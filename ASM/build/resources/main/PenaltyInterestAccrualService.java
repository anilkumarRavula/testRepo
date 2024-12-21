/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Sri Satya
 */

public class PenaltyInterestAccrualService {


    public List<RepaymentAmount> getPenaltyDues(long trancheId, LocalDate dueDate, LoanAccount loanAccount,
                                                RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO,
                                                List<LoanPenaltyInterestHistoryEntity> penaltyInterestHistoryEntities) {
        Supplier<TpathRecorder> recorder = Tpath.INSTANCE.startAndRecord(this.getClass().getTypeName(),"getPenaltyDues");

        List<RepaymentAmount> penaltyDues = Lists.newArrayList();

        if (penaltyInterestHistoryEntities.isEmpty()) return penaltyDues;
        List<LoanPenaltyInterestHistoryEntity> penaltyHistoryEntities = penaltyInterestHistoryEntities
                .stream()
                .filter(x -> x.getStartDate().isBefore(dueDate))
                .collect(Collectors.toList());
        if (penaltyHistoryEntities.isEmpty()) return penaltyDues;

        Optional<RestructureEventEntity> latestArrearRestructureEvent = restructureEventRepository
                .findTopByLoanIdAndIsReversedFalseAndIsArrearsRestructuringTrueAndInterestTreatmentOrderByIdDesc(
                        loanAccount.getIdentifier(), InterestTreatmentType.INTEREST);

        if (Objects.isNull(loanMasterDTO)) {
            loanMasterDTO = loanMasterDTOUtil.create(loanAccount);
        }

        DisbursementData disbursementData;
        if (repositoryDTO != null || loanMasterDTO != null) { //sonar fix
            Stream<DisbursementData> disbursementDataList = Objects.nonNull(repositoryDTO) ?
                    repositoryDTO.getLoanAccount().getDisbursementDatas().stream()
                    : loanMasterDTO.getDisbursementDataList().stream();
            Optional<DisbursementData> disbursementDataOptional = disbursementDataList
                    .filter(d -> d.getIdentifier().equals(trancheId))
                    .findFirst();
            disbursementData = disbursementDataOptional.orElseGet(() -> trancheUtilService.getDisbursementDataForTrancheId(trancheId));
        } else {
            disbursementData = trancheUtilService.getDisbursementDataForTrancheId(trancheId);
        }
        DisbursementData finalDisbursementData = disbursementData;

        List<LoanScheduleModelRepaymentPeriod> dueInstallments = loanAccount.getRepaymentPeriodInstallments().stream()
                .filter(x -> LocalDate.parse(x.getInstallmentDate()).isBefore(dueDate)
                        && x.isScheduleIPD()
                        && x.getTrancheSequenceNumber().equals(finalDisbursementData.getDisbursementSequenceNumber())
                        && !x.isDummyScheduleEntry())
                .collect(Collectors.toList());

        for (LoanPenaltyInterestHistoryEntity historyEntity : penaltyHistoryEntities) {
            if (Objects.nonNull(historyEntity.getEndDate())) {
                List<LoanScheduleModelRepaymentPeriod> installmentsInBetween = dueInstallments.stream()
                        .filter(x -> DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z").isAfter(historyEntity.getStartDate())
                                && DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z").isBefore(historyEntity.getEndDate()))
                        .collect(Collectors.toList());
                for (LoanScheduleModelRepaymentPeriod installment : installmentsInBetween) {
                    LocalDate endDate = DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z");
                    if (!latestArrearRestructureEvent.isPresent() || endDate.isAfter(latestArrearRestructureEvent.get().getRestructureDate())) {
                        addToPenaltyDues(penaltyDues, loanAccount, disbursementData, historyEntity, endDate, repositoryDTO, loanMasterDTO);
                    }
                }
                if (!latestArrearRestructureEvent.isPresent()
                        || historyEntity.getEndDate().isAfter(latestArrearRestructureEvent.get().getRestructureDate())) {
                    addToPenaltyDues(penaltyDues, loanAccount, disbursementData, historyEntity,historyEntity.getEndDate(), repositoryDTO, loanMasterDTO);
                }
            } else {
                List<LoanScheduleModelRepaymentPeriod> installmentsInBetween = dueInstallments.stream()
                        .filter(x -> DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z").isAfter(historyEntity.getStartDate())
                                && DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z").isBefore(dueDate))
                        .collect(Collectors.toList());
                for (LoanScheduleModelRepaymentPeriod installment : installmentsInBetween) {
                    LocalDate endDate = DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z");
                    if (!latestArrearRestructureEvent.isPresent() || endDate.isAfter(latestArrearRestructureEvent.get().getRestructureDate())) {
                        addToPenaltyDues(penaltyDues, loanAccount, disbursementData, historyEntity,endDate, repositoryDTO, loanMasterDTO);
                    }
                }
                if (!latestArrearRestructureEvent.isPresent() || dueDate.isAfter(latestArrearRestructureEvent.get().getRestructureDate())) {
                    addToPenaltyDues(penaltyDues, loanAccount, disbursementData, historyEntity,dueDate, repositoryDTO, loanMasterDTO);
                }
            }
        }
        fetchDeferredPenaltyDues(loanAccount,trancheId,dueDate,loanMasterDTO,penaltyDues,null);

        Tpath.INSTANCE.addAndEnd(recorder.get());
        return penaltyDues;
    }

    private void addToPenaltyDues(List<RepaymentAmount> penaltyDues,LoanAccount loanAccount,
                                  DisbursementData disbursementData,LoanPenaltyInterestHistoryEntity historyEntity,
                                  LocalDate endDate,RepositoryDTO repositoryDTO,LoanMasterDTO loanMasterDTO) {
        BigDecimal penaltyCalculated = calculatePenaltyBetween(loanAccount, disbursementData,
                historyEntity, endDate, repositoryDTO, loanMasterDTO);
        List<DeferredDemandEntity> deferredDemands = deferDemandService.fetchAllDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier());
        List<DeferredDemandEntity> deferredDemandsForTranche = Lists.newArrayList();
        deferredDemands.forEach(dd -> {
            Optional<Demand> demand = loanMasterDTO.getDemandList().stream().filter(d -> d.getId().equals(dd.getDemandId())).findFirst();
            if(demand.isPresent() && demand.get().getTrancheId().equals(disbursementData.getIdentifier())){
                deferredDemandsForTranche.add(dd);
            }

        });
        boolean principalDueBasedCalc = historyEntity.getPenaltyCalculationMethod().equals(PenalCalculationMethod.TPD);

        BigDecimal existingDueBetween = BigDecimal.ZERO;
        if (repositoryDTO != null) {
            List<Demand> demands = repositoryDTO.getDemands().stream().filter(x -> !x.getDeleted()
                            && !x.getReversed() && x.getDemandType() == DemandType.PENALTY
                            && disbursementData.getIdentifier().equals(x.getTrancheId())
                            && !isDeferredPenaltyDemandForPrincipalDue(deferredDemandsForTranche,x,principalDueBasedCalc)
                            && DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(historyEntity.getStartDate())
                            && !DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(endDate))
                    .collect(Collectors.toList());
            for (Demand d : demands) {
                existingDueBetween = existingDueBetween.add(d.getDueAmount());
                BigDecimal collectedAmount = repositoryDTO.getDemandCollections().stream()
                        .filter(x -> !x.getReversed() && x.getDemandId().equals(d.getId()))
                        .map(x -> x.getApportionedAndTransferredAmount().add(x.getWaivedAmount()).add(x.getCapitalizedAmount()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dueAmount = d.getDueAmount().subtract(collectedAmount);
                boolean isDueAdded = penaltyDues.stream().anyMatch(x-> Objects.nonNull(x.getDemandId()) && x.getDemandId().equals(d.getId()))
                        || repositoryDTO.getDemands().stream().anyMatch(x-> Objects.nonNull(x.getId()) && x.getId().equals(d.getId()));
                if (dueAmount.compareTo(BigDecimal.ZERO) > 0 && !isDueAdded) {
                    RepaymentAmount penaltyGenerated = generatePenaltyAmount(d.getId(), d.getDemandDate(),
                            d.getInstallmentNo(), dueAmount, disbursementData.getIdentifier(), loanMasterDTO.getLoanProduct());
                    penaltyDues.add(penaltyGenerated);
                }
            }
        } else {
            List<Demand> demands = loanMasterDTO.getDemandList().stream().filter(x -> !x.getReversed()
                            && !x.getDeleted() && x.getDemandType() == DemandType.PENALTY
                            && disbursementData.getIdentifier().equals(x.getTrancheId())
                            && !isDeferredPenaltyDemandForPrincipalDue(deferredDemandsForTranche,x,principalDueBasedCalc)
                            && DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(historyEntity.getStartDate())
                            && !DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(endDate))
                    .collect(Collectors.toList());

            for (Demand d : demands) {
                existingDueBetween = existingDueBetween.add(d.getDueAmount());
                BigDecimal collectedAmount = loanMasterDTO.getDemandCollectionList().stream()
                        .filter(x -> !x.getReversed() && x.getDemandId().equals(d.getId()))
                        .map(x -> x.getApportionedAndTransferredAmount().add(x.getWaivedAmount()).add(x.getCapitalizedAmount()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dueAmount = d.getDueAmount().subtract(collectedAmount);
                boolean isDueAdded = penaltyDues.stream().anyMatch(x -> Objects.nonNull(x.getDemandId()) && x.getDemandId().equals(d.getId()));
                if (dueAmount.compareTo(BigDecimal.ZERO) > 0 && !isDueAdded) {
                    RepaymentAmount penaltyGenerated = generatePenaltyAmount(d.getId(), d.getDemandDate(),
                            d.getInstallmentNo(), dueAmount, disbursementData.getIdentifier(), loanMasterDTO.getLoanProduct());
                    penaltyDues.add(penaltyGenerated);
                }
            }
        }
        BigDecimal currentPenaltyDue = penaltyDues.stream().filter(x -> x.getDemandId() == null
                        && DateConverter.dateFromIsoString(x.getDueDate() + "Z").isAfter(historyEntity.getStartDate())
                        && !DateConverter.dateFromIsoString(x.getDueDate() + "Z").isAfter(endDate))
                .map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        penaltyCalculated = penaltyCalculated.subtract(existingDueBetween).subtract(currentPenaltyDue);

        BigDecimal deferredAmount = deferredDemandsForTranche
                .stream()
                .map(DeferredDemandEntity::getCurrentDeferredAmount)
                .filter(NumberUtils::isGreaterThanZero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        penaltyCalculated = penaltyCalculated.subtract(deferredAmount);

        if (penaltyCalculated.compareTo(BigDecimal.ZERO) > 0) {
            long installmentNo = getInstallmentNoForPenaltyDemand(loanAccount,disbursementData,endDate);
            RepaymentAmount penaltyGenerated = generatePenaltyAmount(null, endDate.toString(),
                    installmentNo, penaltyCalculated, disbursementData.getIdentifier(), loanMasterDTO.getLoanProduct());
            penaltyDues.add(penaltyGenerated);
        }
    }

    private void fetchDeferredPenaltyDues(LoanAccount loanAccount,Long trancheId, LocalDate eventDate, LoanMasterDTO loanMasterDTO,
                                          List<RepaymentAmount> penaltyDues,List<DeferredDemandEntity> deferredDemandEntities) {

        if(!Objects.nonNull(deferredDemandEntities)){
            deferredDemandEntities = deferDemandService
                    .fetchAllNonZeroDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier());
        }
        deferredDemandEntities.forEach(dd -> {
            if (dd.nextDeferDate().isAfter(eventDate)) {
                Optional<Demand> d = loanMasterDTO.getDemandList()
                        .stream()
                        .filter(x -> x.getId().equals(dd.getDemandId()))
                        .findFirst();
                if (d.isPresent() && d.get().getTrancheId().equals(trancheId)) {
                    RepaymentAmount penaltyGenerated = generatePenaltyAmount(null, eventDate.toString(),
                            d.get().getInstallmentNo(), dd.getCurrentDeferredAmount(), d.get().getTrancheId(), loanMasterDTO.getLoanProduct());
                    penaltyGenerated.setDeferId(dd.getId());
                    penaltyDues.add(penaltyGenerated);
                }
            }
        });

        List<Demand> deferredPenaltyAccruedDemands = loanMasterDTO.getDemandList().stream()
                .filter(d -> !d.getDeleted() && !d.getReversed() && d.getDemandType().equals(DemandType.PENALTY)
                        && d.getTrancheId().equals(trancheId)
                        && LocalDate.parse(d.getDemandDate()).isAfter(eventDate))
                .collect(Collectors.toList());
        deferredPenaltyAccruedDemands.forEach(d -> {
            RepaymentAmount penaltyGenerated = generatePenaltyAmount(d.getId(), eventDate.toString(),
                    d.getInstallmentNo(), d.getDueAmount(), d.getTrancheId(), loanMasterDTO.getLoanProduct());
            penaltyDues.add(penaltyGenerated);
        });
    }

    private boolean isDeferredPenaltyDemandForPrincipalDue(List<DeferredDemandEntity> deferredDemandEntities, Demand demand,boolean principalDueFlag){
        return principalDueFlag && deferredDemandEntities.stream().anyMatch(x -> !x.isReversed()
                && x.nextDeferDate().equals(LocalDate.parse(demand.getDemandDate()))
                && x.getOriginalDeferredAmount().compareTo(demand.getDueAmount()) == 0);
    }

    public void stopPenaltyEventHandler(LoanAccount loanAccount, PenaltyWindowEventEntity penaltyWindowEventEntity,
                                        LoanPenaltyInterestHistoryEntity loanPenaltyInterestHistoryEntity,
                                        RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO) {
        Map<Long, List<LoanScheduleModelRepaymentPeriod>> trancheWiseInstallments = TrancheUtilsForOverdue.getSortedTrancheWiseScheduleForLoan(loanAccount);
        if (loanMasterDTO == null) {
            loanMasterDTO = loanMasterDTOUtil.create(loanAccount);
        }
        for (Map.Entry<Long, List<LoanScheduleModelRepaymentPeriod>> tranche : trancheWiseInstallments.entrySet()) {
            List<LoanScheduleModelRepaymentPeriod> installments = tranche.val();
            List<LoanScheduleModelRepaymentPeriod> dueInstallments = installments.stream().filter(x ->
                    !DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z")
                            .isBefore(loanPenaltyInterestHistoryEntity.getStartDate())
                            && !DateConverter.dateFromIsoString(x.getInstallmentDate() + "Z")
                            .isAfter(loanPenaltyInterestHistoryEntity.getEndDate())
                            && genericLoanServices.isScheduleIPD(x)).collect(Collectors.toList());
            DisbursementData disbursementData = trancheUtilService.getDisbursementDataForTrancheId(
                    loanAccount.getDisbursementDatas(), tranche.getKey());
            List<Transaction> transactionMatrix = Lists.newArrayList();
            transactionMatrix.add(new Transaction(loanPenaltyInterestHistoryEntity.getStartDate().toString(), BigDecimal.ZERO));
            transactionMatrix.add(new Transaction(loanPenaltyInterestHistoryEntity.getEndDate().toString(), BigDecimal.ZERO));
            for (LoanScheduleModelRepaymentPeriod dueInstallment : dueInstallments) {
                transactionMatrix.add(new Transaction(dueInstallment.getInstallmentDate(), BigDecimal.ZERO));
            }
            transactionMatrix.sort(Comparator.comparing(Transaction::getTransactionDate));

            List<DemandEntity> currentTransactionDemands = Lists.newArrayList();
            for (int index = 1; index < transactionMatrix.size(); index++) {
                LocalDate endDate = DateConverter.dateFromIsoString(transactionMatrix.get(index).getTransactionDate() + "Z");
                createDemands(loanAccount, disbursementData, loanPenaltyInterestHistoryEntity, endDate,
                        penaltyWindowEventEntity, repositoryDTO, loanMasterDTO, currentTransactionDemands);
            }
        }
        createDemandsForRestructuredDemands(loanAccount,penaltyWindowEventEntity.getId(),
                penaltyWindowEventEntity.getEventDate(),loanMasterDTO,"Deferred Demand Post Stop Event");
    }

    private void createDemands(LoanAccount loanAccount, DisbursementData disbursementData,
                               LoanPenaltyInterestHistoryEntity historyEntity, LocalDate endDate,
                               PenaltyWindowEventEntity penaltyWindowEventEntity, RepositoryDTO repositoryDTO,
                               LoanMasterDTO loanMasterDTO,List<DemandEntity> currentTransactionDemands) {
        BigDecimal penaltyCalculated = calculatePenaltyBetween(loanAccount, disbursementData,historyEntity,
                endDate,repositoryDTO, loanMasterDTO);
        Long installmentNo = getInstallmentNoForPenaltyDemand(loanAccount, disbursementData, endDate);
        List<Demand> demands;
        if (repositoryDTO != null) {
            demands = repositoryDTO.getDemands().stream().filter(d -> d.getLoanId().equals(loanAccount.getIdentifier())
                            && (d.getTrancheId() != null && d.getTrancheId().equals(disbursementData.getIdentifier()))
                            && d.getDemandType() == DemandType.PENALTY && LocalDate.parse(d.getDemandDate()).isAfter(historyEntity.getStartDate())
                            && !d.getReversed() && !d.getDeleted())
                    .collect(Collectors.toList());
        } else {
            demands = loanMasterDTO.getDemandList().stream().filter(x -> x.getTrancheId() != null
                            && x.getTrancheId().equals(disbursementData.getIdentifier()) && !x.getReversed() && !x.getDeleted()
                            && x.getDemandType() == DemandType.PENALTY && LocalDate.parse(x.getDemandDate()).isAfter(historyEntity.getStartDate()))
                    .sorted(Comparator.comparing(Demand::getId)).collect(Collectors.toList());
        }
        BigDecimal currentPenaltyDue = currentTransactionDemands.stream().filter(x ->
                        DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(historyEntity.getStartDate())
                                && !DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(endDate))
                .map(DemandEntity::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal existingDemandsDue = demands.stream().map(Demand::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        penaltyCalculated = penaltyCalculated.subtract(existingDemandsDue).subtract(currentPenaltyDue);

        List<DeferredDemandEntity> deferredDemands = deferDemandService.fetchAllNonZeroDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier());
        List<DeferredDemandEntity> deferredDemandsForTranche = Lists.newArrayList();
        deferredDemands.forEach(dd -> {
            Optional<Demand> demand = loanMasterDTO.getDemandList().stream().filter(d -> d.getId().equals(dd.getDemandId())).findFirst();
            if(demand.isPresent() && demand.get().getTrancheId().equals(disbursementData.getIdentifier())){
                deferredDemandsForTranche.add(dd);
            }
        });
        BigDecimal deferredAmount = deferredDemandsForTranche
                .stream()
                .map(DeferredDemandEntity::getCurrentDeferredAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        penaltyCalculated = penaltyCalculated.subtract(deferredAmount);

        if (NumberUtils.isGreaterThanZero(penaltyCalculated)) {
            DemandEntity penaltyDemandEntity = DemandEntity.createDemandEntity(loanAccount.getIdentifier(),
                    disbursementData.getIdentifier(), EventType.PNLTYINT, penaltyWindowEventEntity.getId(),
                    null, null, installmentNo, endDate, BigDecimal.ZERO,
                    penaltyCalculated, loanAccount.getCurrencyCode(), "Stop Penalty");
            currentTransactionDemands.add(demandRepository.save(penaltyDemandEntity));
        }
    }

    private Long getInstallmentNoForPenaltyDemand(LoanAccount loanAccount, DisbursementData disbursementData,LocalDate demandDate) {
        List<LoanScheduleModelRepaymentPeriod> sortedInstallments = loanAccount.getRepaymentPeriodInstallments()
                .stream()
                .filter(x -> x.getTrancheSequenceNumber().equals(disbursementData.getDisbursementSequenceNumber()) && (x.getInterestPayingIpd() || x.getPrincipalPayingIpd()))
                .sorted(Comparator.comparing(LoanScheduleModelRepaymentPeriod::getInstallmentDate))
                .collect(Collectors.toList());
        for (LoanScheduleModelRepaymentPeriod installment : sortedInstallments) {
            if (!DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z").isBefore(demandDate)) {
                return (long) installment.getPeriodNumber();
            }
        }
        boolean isRepaidAfterLastIPDFlag = isRepaidOnOrAfterLastIpd(loanAccount, disbursementData.getIdentifier(), demandDate);
        return isRepaidAfterLastIPDFlag ? (long) sortedInstallments.get(sortedInstallments.size() - 1).getPeriodNumber() :
                (long) sortedInstallments.get(sortedInstallments.size() - 1).getPeriodNumber() + 1;
    }

    // overloading get penalty History for BULK API Get Call.
    public PenaltyHistory getPenaltyHistory(Long loanId) {
        return getPenaltyHistory(loanId, false);
    }

    private PenaltyHistory getPenaltyHistory(Long loanId, boolean flag) {
        PenaltyHistory penaltyHistory = new PenaltyHistory();
        penaltyHistory.setLoanId(loanId);
        List<LoanPenaltyInterestHistory> loanPenaltyInterestHistoryList = new ArrayList<>();
        penaltyHistory.setPenaltyInterestAccruedTillDate(BigDecimal.ZERO);
        penaltyHistory.setLoanPenaltyInterestHistoryList(loanPenaltyInterestHistoryList);
        penaltyHistory.setPenaltyAccruedButNotPaid(BigDecimal.ZERO);

        List<LoanPenaltyInterestHistoryEntity> loanPenaltyInterestHistoryEntities = flag ?
                loanPenaltyIntHistoryRepository.findByLoanIdOrderById(loanId)
                : loanPenaltyIntHistoryRepository.findByLoanIdAndIsDeletedFalseOrderById(loanId);

        if (loanPenaltyInterestHistoryEntities.isEmpty()) {
            return penaltyHistory;
        }
        LoanAccount loanAccount = loanAccountService.getLoanAccountDetails(loanId);
        LoanMasterDTO loanMasterDTO = getLoanMasterDTOForPenalty(loanAccount, loanPenaltyInterestHistoryEntities);
        if (flag) {
            BigDecimal penaltyAccruedButNotPaid = loanAccountService.getPenaltyAccruedButNotPaid(loanAccount, loanMasterDTO);
            penaltyHistory.setPenaltyAccruedButNotPaid(penaltyAccruedButNotPaid);
        }
        for (LoanPenaltyInterestHistoryEntity loanPenaltyInterestHistoryEntity : loanPenaltyInterestHistoryEntities) {
            LoanPenaltyInterestHistory loanPenaltyInterestHistory = PenaltyMapper.map(loanPenaltyInterestHistoryEntity);

            // Setting Only for bulk API
            if (flag) {
                loanPenaltyInterestHistory.setDeleted(loanPenaltyInterestHistoryEntity.getDeleted());
            }
            // FOR BULK API Check
            if (loanPenaltyInterestHistoryEntity.getDeleted().equals(false)) {

                if (loanPenaltyInterestHistoryEntity.getEndDate() != null) {
                    BigDecimal dueCalculated = demandRepository.findDuesBetween(loanId, DemandType.PENALTY, false,
                            loanPenaltyInterestHistoryEntity.getStartDate(), loanPenaltyInterestHistoryEntity.getEndDate()).orElse(BigDecimal.ZERO);
                    loanPenaltyInterestHistory.setPenaltyAmount(dueCalculated);
                } else {
                    List<TrancheBalances> activeTranches = trancheUtilServiceImpl.getTranchesForLoan(loanId)
                            .stream().sorted(Comparator.comparing(TrancheBalances::getId))
                            .filter(trancheBalancesEntity -> trancheBalancesEntity.getPrincipalBalance().compareTo(BigDecimal.ZERO) > 0)
                            .collect(Collectors.toList());

                    penaltyHistory.setCurrentPenaltyRate(loanPenaltyInterestHistory.getPenaltyRate());
                    penaltyHistory.setCurrentPenalCalculationMethod(loanPenaltyInterestHistory.getPenalCalculationMethod());
                    penaltyHistory.setCurrentPenalCalculationIntMethod(loanPenaltyInterestHistory.getPenalCalculationInterestMethod());
                    penaltyHistory.setCurrentPenaltyStartDate(loanPenaltyInterestHistory.getStartDate());
                    for (TrancheBalances activeTranche : activeTranches) {
                        DisbursementData disbursementData = trancheUtilService.getDisbursementDataForTrancheId(
                                loanAccount.getDisbursementDatas(), activeTranche.getTrancheId());
                        penaltyHistory.setPenaltyInterestAccruedTillDate(penaltyHistory.getPenaltyInterestAccruedTillDate().add(
                                calculatePenaltyBetween(loanAccount, disbursementData,
                                        loanPenaltyInterestHistoryEntity, LocalDate.now(), null, loanMasterDTO)));
                    }
                }
            }
            loanPenaltyInterestHistoryList.add(loanPenaltyInterestHistory);


        }

        penaltyHistory.setLoanPenaltyInterestHistoryList(loanPenaltyInterestHistoryList);
        return penaltyHistory;
    }

    private boolean isRepaidOnOrAfterLastIpd(LoanAccount loanAccount, Long trancheId, LocalDate repaymentDate) {
        Map<Long, List<LoanScheduleModelRepaymentPeriod>> trancheWiseInstallment = TrancheUtilsForOverdue.getSortedTrancheWiseScheduleForLoan(loanAccount);
        List<LoanScheduleModelRepaymentPeriod> installments = trancheWiseInstallment.get(trancheId);
        return !repaymentDate.isBefore(
                DateConverter.dateFromIsoString(installments.get(installments.size() - 1).getDueDate() + "Z"));
    }

    public RepaymentAmount generatePenaltyAmount(Long demandId, String dueDate, long periodNumber, BigDecimal penaltyCalculated,
                                                 Long trancheId, LoanProduct loanProduct) {
        RepaymentAmount penalty = new RepaymentAmount();
        penalty.setDemandId(demandId);
        if (penaltyCalculated.compareTo(BigDecimal.ZERO) > 0) {
            penalty.setDueAmount(RoundOffUtil.roundToMultiplesOf(Optional.of(loanProduct), penaltyCalculated));
        }
        penalty.setActualDueAmount(penalty.getDueAmount());
        penalty.setTrancheId(trancheId);
        penalty.setInstallmentNo(periodNumber);
        penalty.setDueDate(dueDate);
        penalty.setDemandType(DemandType.PENALTY);
        return penalty.getDueAmount().compareTo(BigDecimal.ZERO) > 0 ? penalty : null;
    }

    public BigDecimal calculatePenaltyBetween(LoanAccount loanAccount, DisbursementData disbursementData,
                                              LoanPenaltyInterestHistoryEntity historyEntity, LocalDate endDate,
                                              RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO) {
        return calculatePenaltyBetween(loanAccount, disbursementData, historyEntity, endDate, repositoryDTO, loanMasterDTO, null);
    }

    public BigDecimal calculatePenaltyBetween(LoanAccount loanAccount, DisbursementData disbursementData,
                                              LoanPenaltyInterestHistoryEntity historyEntity, LocalDate endDate,
                                              RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO,
                                              List<RepaymentNotificationInterestAndFeeBreakUp> penaltyInterestDetails) {
        BigDecimal penaltyInterest = BigDecimal.ZERO;
        BigDecimal outstandingPrincipal = BigDecimal.ZERO;
        BigDecimal penaltyInterestRate = historyEntity.getPenaltyInterestRate();

        //Calculate 8 decimal Penalty Interest on outstanding balances from penalty Start Date to Repayment Date

        List<LoanTransaction> transactionsList = getFilteredEventTransaction(Objects.nonNull(repositoryDTO) ?
                repositoryDTO.getEventMasterList() : loanMasterDTO.getEventMasterList(), loanAccount.getIdentifier(), historyEntity.getStartDate(), endDate);
        //to add the transactions if permanent grace is defined on loan account or penalty is on principal due
        transactionsList = getPermanentGraceTransactionsForPenalty(loanAccount, disbursementData, historyEntity.getStartDate(),
                endDate, transactionsList, historyEntity.getPenaltyCalculationMethod(), historyEntity.getPenalCalculationInterestMethod());

        BigDecimal penaltyTransferred = loanMasterDTO.getTrancheTransferList().stream()
                .filter(t -> t.getActive() && t.getTrancheId().equals(disbursementData.getIdentifier()) &&
                        !LocalDate.parse(disbursementData.getActualDisbursementDate()).isBefore(historyEntity.getStartDate()) &&
                        !LocalDate.parse(disbursementData.getActualDisbursementDate()).isAfter(endDate))
                .map(TrancheTransfer::getPenaltyAccruedNotPaid).reduce(BigDecimal.ZERO,BigDecimal::add);
        penaltyInterest = penaltyInterest.add(penaltyTransferred);


        LoanTransaction txnForLastDate = new LoanTransaction();
        txnForLastDate.setTransactionDate(endDate.toString());
        transactionsList.add(txnForLastDate);
        transactionsList.sort(Comparator.comparing(LoanTransaction::getTransactionDate));

        LocalDate calcStartDate = historyEntity.getStartDate();
        for (LoanTransaction txn : transactionsList) {
            LocalDate calcEndDate = LocalDate.parse(txn.getTransactionDate());
            DaysInYearType daysInYearType = TrancheUtilsForOverdue.getDaysInYearTypeAsOfDate(loanAccount, calcStartDate, loanMasterDTO.getRestructureList());
            DaysInMonthType daysInMonthType = TrancheUtilsForOverdue.getDaysInMonthTypeAsOfDate(loanAccount, calcStartDate, loanMasterDTO.getRestructureList());
            if (historyEntity.getPenaltyCalculationMethod().equals(PenalCalculationMethod.OPPL)) {
                outstandingPrincipal = TrancheUtilsForOverdue.getOutstandingPrincipalForTrancheAsOfDateWithGrace(
                        loanAccount, disbursementData, repositoryDTO, calcStartDate, false,
                        transactionsList, loanMasterDTO).get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);
            } else if (historyEntity.getPenaltyCalculationMethod().equals(PenalCalculationMethod.TPD)) {
                outstandingPrincipal = OverdueCalculationService.getPrincipalDueForTrancheAsOfDateWithGrace(loanAccount,
                                disbursementData, repositoryDTO, calcStartDate, BigDecimal.ZERO, loanMasterDTO)
                        .get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);
            }
            RepaymentTransaction interestDataDetails = getInterestAmount(loanMasterDTO, loanAccount,
                    disbursementData.getIdentifier(), calcStartDate, historyEntity.getPenalCalculationInterestMethod(), repositoryDTO);
            BigDecimal penaltyInterestForBreakUp = getPenaltyForPeriod(interestDataDetails, calcStartDate, calcEndDate,
                    outstandingPrincipal, daysInMonthType, daysInYearType, loanAccount, disbursementData, penaltyInterestRate,
                    historyEntity, repositoryDTO, loanMasterDTO);
            if (penaltyInterestDetails != null) {
                BigDecimal daysBetweenPeriod = TrancheUtilsForOverdue.getNumberOfDaysBetween(calcStartDate.toString(), calcEndDate.toString(), daysInMonthType,
                        loanAccount.getExpectedMaturityDate());
                penaltyInterestDetails.add(new RepaymentNotificationInterestAndFeeBreakUp(calcStartDate, calcEndDate, null, outstandingPrincipal,
                        penaltyInterestRate, daysBetweenPeriod, penaltyInterestForBreakUp));
            }
            penaltyInterest = penaltyInterest.add(penaltyInterestForBreakUp);
            calcStartDate = calcEndDate;
        }
        //Round penalty interest to 2 decimal
        penaltyInterest = RoundOffUtil.roundToMultiplesOf(Optional.of(loanMasterDTO.getLoanProduct()), penaltyInterest);
        return penaltyInterest;
    }

    private List<LoanTransaction> getFilteredEventTransaction(List<EventMaster> eventMasterList, Long loanId, LocalDate startDate, LocalDate endDate) {
        //Add all events affecting Outstanding balance of tranche as it will impact Penalty
        EnumSet<EventType> eventsToBeConsidered = EnumSet.of(EventType.DSBRSMNT, EventType.REPAYMNT, EventType.INTROLP,EventType.PENROLP,
                EventType.CAPFEE, EventType.ADHOCFEE, EventType.PRFEDECO, EventType.MINTCAP, EventType.RATEREST, EventType.RESTRCTR, EventType.TRNSFOUT, EventType.TRNSFRIN);

        List<EventMaster> eventMasterFilterList = eventMasterList.stream().filter(x -> !x.getReversed()
                        && x.getLoanId().equals(loanId)
                        && eventsToBeConsidered.contains(x.getEventType())
                        && !LocalDate.parse(x.getEventDate()).isBefore(startDate)
                        && !LocalDate.parse(x.getEventDate()).isAfter(endDate))
                .sorted(Comparator.comparing(EventMaster::getId)).collect(Collectors.toList());
        List<LoanTransaction> transactionsList = new ArrayList<>();
        for (EventMaster eventMaster : eventMasterFilterList) {
            LoanTransaction loanTransaction = new LoanTransaction();
            loanTransaction.setId(eventMaster.getId());
            loanTransaction.setEventType(eventMaster.getEventType());
            loanTransaction.setTransactionDate(eventMaster.getEventDate());
            transactionsList.add(loanTransaction);
        }
        return transactionsList;
    }

    public BigDecimal getPenaltyForPeriod(RepaymentTransaction interestDuesDetails, LocalDate d1, LocalDate d2,
                                          BigDecimal principalForPenalty, DaysInMonthType daysInMonthType,
                                          DaysInYearType daysInYearType, LoanAccount loanAccount, DisbursementData disbursementData,
                                          BigDecimal penaltyInterestRate, LoanPenaltyInterestHistoryEntity penaltyHistoryEntity,
                                          RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO) {
        if (penaltyHistoryEntity.getPenalCalculationInterestMethod().equals(PenalCalculationInterestMethod.NONE)
                || repaymentUtilService.checkIfRollupPendingForAnyIpds(loanAccount, d1, repositoryDTO != null ? repositoryDTO.getDemands() : loanMasterDTO.getDemandList())) {
            return OverdueCalculationService.calculateOverdueBetween(penaltyInterestRate,
                    principalForPenalty, d1, d2, daysInMonthType, daysInYearType, loanAccount);
        }
        BigDecimal penaltyForPeriod = BigDecimal.ZERO;
        BigDecimal interestDues = interestDuesDetails.getInterestDetails().stream().map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal od = interestDuesDetails.getOverdueInterestDetails().stream().map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ianp = interestDuesDetails.getInterestAccruedButNotPaid();

        if (d1.isBefore(d2)) {
            BigDecimal principalDue = OverdueCalculationService.getPrincipalDueForTrancheAsOfDateWithGrace(loanAccount,
                    disbursementData, repositoryDTO, d1, BigDecimal.ZERO, loanMasterDTO).get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);

            BigDecimal rate = EffectiveRateService.findEffectiveRate(d1.toString(), loanAccount, EventType.INTACCRL, disbursementData.getIdentifier(), loanMasterDTO,true);
            BigDecimal oneDayOd = OverdueCalculationService.calculateOverdueBetween(rate,
                    principalDue, d1, d1.plusDays(1), daysInMonthType, daysInYearType, loanAccount);
            BigDecimal oneDayIanp = BigDecimal.ZERO;
            if (penaltyHistoryEntity.getPenalCalculationInterestMethod().equals(PenalCalculationInterestMethod.INTALL)) {
                BigDecimal outstandingPrincipal = TrancheUtilsForOverdue.getOutstandingPrincipalForTrancheAsOfDateWithGrace(loanAccount, disbursementData, repositoryDTO,
                        d1, false, null, loanMasterDTO).get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);
                oneDayIanp = OverdueCalculationService.calculateOverdueBetween(rate,
                        outstandingPrincipal, d1, d1.plusDays(1), daysInMonthType, daysInYearType, loanAccount);
                oneDayIanp = oneDayIanp.subtract(oneDayOd);
                if (oneDayIanp.compareTo(BigDecimal.ZERO) < 0) {
                    oneDayIanp = BigDecimal.valueOf(Math.abs(oneDayIanp.doubleValue()));
                }
            }

            while (d1.isBefore(d2)) {
                od = od.add(oneDayOd);
                ianp = ianp.add(oneDayIanp);

                BigDecimal finalPrincipal = principalForPenalty.add(interestDues).add(RoundOffUtil.roundToMultiplesOf(Optional.of(loanMasterDTO.getLoanProduct()), od));
                if (penaltyHistoryEntity.getPenalCalculationInterestMethod().equals(PenalCalculationInterestMethod.INTALL)) {
                    if (ianp != null)
                        finalPrincipal = finalPrincipal.add(RoundOffUtil.roundToMultiplesOf(Optional.of(loanMasterDTO.getLoanProduct()), ianp));
                }
                BigDecimal tmp = OverdueCalculationService.calculateOverdueBetween(penaltyInterestRate,
                        finalPrincipal, d1, d1.plusDays(1), daysInMonthType, daysInYearType, loanAccount);
                penaltyForPeriod = penaltyForPeriod.add(tmp);

                d1 = d1.plusDays(1);
            }
        }
        return penaltyForPeriod;
    }

    public BigDecimal getPrincipalForPenaltyForDate(RepaymentTransaction interestDuesDetails, LocalDate date,
                                                    BigDecimal principalForPenalty,
                                                    LoanAccount loanAccount, DisbursementData disbursementData,
                                                    PenalCalculationInterestMethod penaltyInterestMethod,
                                                    RepositoryDTO repositoryDTO, LoanMasterDTO loanMasterDTO) {
        if (PenalCalculationInterestMethod.NONE.equals(penaltyInterestMethod)) {
            return principalForPenalty;
        }

        DaysInYearType daysInYearType = TrancheUtilsForOverdue.getDaysInYearTypeAsOfDate(loanAccount, date, loanMasterDTO.getRestructureList());
        DaysInMonthType daysInMonthType = TrancheUtilsForOverdue.getDaysInMonthTypeAsOfDate(loanAccount, date, loanMasterDTO.getRestructureList());

        BigDecimal oneDayOd;
        BigDecimal oneDayIanp = BigDecimal.ZERO;
        BigDecimal interestDues = interestDuesDetails.getInterestDetails().stream().map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal od = interestDuesDetails.getOverdueInterestDetails().stream().map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ianp = interestDuesDetails.getInterestAccruedButNotPaid();
        BigDecimal rate = EffectiveRateService.findEffectiveRate(date.toString(), loanAccount, EventType.INTACCRL, disbursementData.getIdentifier(), loanMasterDTO,false);

        BigDecimal principalDue = OverdueCalculationService.getPrincipalDueForTrancheAsOfDateWithGrace(loanAccount,
                disbursementData, repositoryDTO, date, BigDecimal.ZERO, loanMasterDTO).get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);
        oneDayOd = OverdueCalculationService.calculateOverdueBetween(rate,
                principalDue, date, date.plusDays(1), daysInMonthType, daysInYearType, loanAccount);

        if (PenalCalculationInterestMethod.INTALL.equals(penaltyInterestMethod)) {
            BigDecimal outstandingPrincipal = TrancheUtilsForOverdue.getOutstandingPrincipalForTrancheAsOfDateWithGrace(loanAccount, disbursementData, null,
                    date, false, null, loanMasterDTO).get(Constants.OUTSTANDING_BALANCE_AFTER_GRACE);
            oneDayIanp = OverdueCalculationService.calculateOverdueBetween(rate,
                    outstandingPrincipal, date, date.plusDays(1), daysInMonthType, daysInYearType, loanAccount);
            oneDayIanp = oneDayIanp.subtract(oneDayOd);
        }
        od = od.add(oneDayOd);
        ianp = ianp.add(oneDayIanp);

        BigDecimal finalPrincipalForPenalty = principalForPenalty.add(interestDues).add(RoundOffUtil.roundToMultiplesOf(Optional.of(loanMasterDTO.getLoanProduct()), od))
                .add(RoundOffUtil.roundToMultiplesOf(Optional.of(loanMasterDTO.getLoanProduct()), ianp));

        interestDuesDetails.setInterestAccruedButNotPaid(ianp);
        RepaymentAmount repaymentAmount = new RepaymentAmount();
        repaymentAmount.setDueAmount(od);
        repaymentAmount.setActualDueAmount(od);
        interestDuesDetails.setOverdueInterestDetails(Arrays.asList(repaymentAmount));

        return finalPrincipalForPenalty;
    }

    public RepaymentTransaction getInterestAmount(LoanMasterDTO loanMasterDTO, LoanAccount loanAccount, long trancheId,
                                                  LocalDate transactionDate, PenalCalculationInterestMethod penaltyInterestMethod,
                                                  RepositoryDTO repositoryDTO) {
        RepaymentTransaction interestDuesDetails = new RepaymentTransaction();
        List<RepaymentAmount> interestDues = new ArrayList<>();
        List<RepaymentAmount> overdues = new ArrayList<>();
        interestDuesDetails.setInterestDetails(interestDues);
        interestDuesDetails.setOverdueInterestDetails(overdues);
        if (PenalCalculationInterestMethod.NONE.equals(penaltyInterestMethod)) {
            return interestDuesDetails;
        }

        ArrayList<LoanScheduleModelRepaymentPeriod> dueInstallments = new ArrayList<>();
        final LoanAccount loanAccountForCalc = repositoryDTO != null ? repositoryDTO.getLoanAccount() : loanAccount;

        Map<Long, List<LoanScheduleModelRepaymentPeriod>> trancheWiseInstallments = TrancheUtilsForOverdue.getSortedTrancheWiseScheduleForLoan(loanAccountForCalc);
        for (LoanScheduleModelRepaymentPeriod installment : trancheWiseInstallments.getOrDefault(trancheId, new ArrayList<>())) {
            if (installment.isScheduleIPD() &&
                    (DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z").isBefore(transactionDate)
                            || (DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z").equals(transactionDate)))) {
                dueInstallments.add(installment);
            }
        }
        interestDues = getInterestDuesAsOfDate(trancheId, transactionDate, dueInstallments, loanMasterDTO, repositoryDTO);
        List<Demand> demandsForCalc = repositoryDTO != null ? repositoryDTO.getDemands() : loanMasterDTO.getDemandList();
        if (!repaymentUtilService.checkIfRollupPendingForAnyIpds(loanAccountForCalc, transactionDate, demandsForCalc)) {
            overdues = repaymentUtilService.getOverdueInterestDetails(loanAccountForCalc, transactionDate, dueInstallments, repositoryDTO, loanMasterDTO);
            if (PenalCalculationInterestMethod.INTALL.equals(penaltyInterestMethod)) {
                BigDecimal ip = OverdueCalculationService.calculateInterestAccruedNotPaidForTranche(loanAccountForCalc, transactionDate, trancheId, overdues, true, repositoryDTO, loanMasterDTO, false, true);
                interestDuesDetails.setInterestAccruedButNotPaid(ip);
            }
        } else {
            logger.info("not computing interest component of penalty since rollup pending for loan:{}", loanAccountForCalc.getIdentifier());
        }

        interestDuesDetails.setInterestDetails(interestDues);
        interestDuesDetails.setOverdueInterestDetails(overdues);
        return interestDuesDetails;
    }

    public List<RepaymentAmount> getInterestDuesAsOfDate(Long trancheId, LocalDate date,
                                                         List<LoanScheduleModelRepaymentPeriod> dueInstallments,
                                                         LoanMasterDTO loanMasterDTO, RepositoryDTO repositoryDTO) {

        List<RepaymentAmount> interestRepaymentAmountList = new ArrayList<>();
        if (dueInstallments.isEmpty()) {
            return interestRepaymentAmountList;
        }

        for (LoanScheduleModelRepaymentPeriod installment : dueInstallments) {
            RepaymentAmount repaymentAmount = new RepaymentAmount();
            repaymentAmount.setActualDueAmount(installment.getInterestDue());
            repaymentAmount.setDueAmount(installment.getInterestDue().subtract(installment.getInterestPaid().add(installment.getTransferredInterest())));
            repaymentAmount.setDueDate(installment.getInstallmentDate());
            repaymentAmount.setInstallmentNo((long) installment.getPeriodNumber());
            repaymentAmount.setDemandType(DemandType.INTEREST);
            repaymentAmount.setTrancheId(installment.getTrancheId());
            interestRepaymentAmountList.add(repaymentAmount);
        }

        List<DemandCollection> filteredDemandCollections = new ArrayList<>();
        List<DemandCollection> demandCollectionsForCalc = repositoryDTO != null ? repositoryDTO.getDemandCollections() : loanMasterDTO.getDemandCollectionList();
        for (DemandCollection demandCollection : demandCollectionsForCalc) {
            if (!demandCollection.getReversed() && !demandCollection.getDemand().getReversed() && demandCollection.getDemand().getDemandType() == DemandType.INTEREST
                    && LocalDate.parse(demandCollection.getCollection().getCollectionDate()).isAfter(date)
                    && demandCollection.getDemand().getTrancheId() != null && demandCollection.getDemand().getTrancheId().equals(trancheId)) {
                filteredDemandCollections.add(demandCollection);
            }
        }
        for (RepaymentAmount interestDueData : interestRepaymentAmountList) {
            for (DemandCollection dc : filteredDemandCollections) {
                Demand demand = dc.getDemand();
                org.apache.fineract.cn.portfolio.api.v1.domain.Collection collection = dc.getCollection();
                if (LocalDate.parse(collection.getCollectionDate()).isAfter(date) && demand.getDemandType() == DemandType.INTEREST && demand.getInstallmentNo().equals(interestDueData.getInstallmentNo())) {
                    interestDueData.setDueAmount(interestDueData.getDueAmount().add(dc.getApportionedAndTransferredAmount()));
                }
            }
        }

        return interestRepaymentAmountList;
    }

    //This method is used to add transactions if penalty interest calculation is on principal due or grace is applicable
    private List<LoanTransaction> getPermanentGraceTransactionsForPenalty(LoanAccount loanAccount, DisbursementData disbursementData,
                                                                          LocalDate fromDate, LocalDate toDate, List<LoanTransaction> transactions,
                                                                          PenalCalculationMethod penalCalculationMethod, PenalCalculationInterestMethod penalCalculationInterestMethod) {

        List<LoanTransaction> transactionList = new ArrayList<>();

        //If start date is b/w installment date and grace date then consider grace date as one txn
        for (LoanScheduleModelRepaymentPeriod installment : loanAccount.getRepaymentPeriodInstallments()) {
            if (((installment.getTrancheId() != null && installment.getTrancheId().equals(disbursementData.getIdentifier())) ||
                    installment.getTrancheSequenceNumber().equals(disbursementData.getDisbursementSequenceNumber()))
                    && this.genericLoanServices.isScheduleIPD(installment)
                    && installment.getPermanentGraceDate() != null
                    && fromDate.isAfter(DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z"))
                    && fromDate.isBefore(DateConverter.dateFromIsoString(installment.getPermanentGraceDate() + "Z"))) {
                LoanTransaction graceTransaction = new LoanTransaction();
                graceTransaction.setTransactionDate(installment.getPermanentGraceDate());
                transactionList.add(graceTransaction);
            }
        }

        //fetch installments that have a grace upto date
        List<LoanScheduleModelRepaymentPeriod> installments = loanAccount.getRepaymentPeriodInstallments().stream().filter(
                        installment -> ((installment.getTrancheId() != null && installment.getTrancheId().equals(disbursementData.getIdentifier())) ||
                                installment.getTrancheSequenceNumber().equals(disbursementData.getDisbursementSequenceNumber())) &&
                                this.genericLoanServices.isScheduleIPD(installment) &&
                                !DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z").isBefore(fromDate) &&
                                !DateConverter.dateFromIsoString(installment.getInstallmentDate() + "Z").isAfter(toDate))
                .sorted(LoanScheduleModelRepaymentPeriod.InstallmentComparator).collect(Collectors.toList());

        installments.forEach(installment -> {
            if (installment.getPermanentGraceDate() != null
                    || penalCalculationMethod.equals(PenalCalculationMethod.TPD)
                    || !penalCalculationInterestMethod.equals(PenalCalculationInterestMethod.NONE)) {

                //Add a transaction for the installment date if penalty is on principal due or grace is applicable
                LoanTransaction graceTransaction = new LoanTransaction();
                graceTransaction.setTransactionDate(installment.getInstallmentDate());
                transactionList.add(graceTransaction);

                //Add a transaction for payByDate or toDate if payByDate is after toDate and grace is applicable
                if (installment.getPermanentGraceDate() != null) {
                    LocalDate payByDate = DateConverter.dateFromIsoString(installment.getPermanentGraceDate() + "Z");
                    payByDate = !payByDate.isBefore(toDate) ? toDate : payByDate;

                    graceTransaction = new LoanTransaction();
                    graceTransaction.setTransactionDate(payByDate.toString());
                    transactionList.add(graceTransaction);
                }
            }
        });

        //To remove the transactions on the same date as permanent grace transactions
        List<LoanTransaction> uniqueTransactions = new ArrayList<>();
        transactions.forEach(transaction -> {
            boolean uniqueFlag = transactionList.stream().noneMatch(graceTransaction -> graceTransaction.getTransactionDate()
                    .equals(transaction.getTransactionDate()));
            if (uniqueFlag) {
                uniqueTransactions.add(transaction);
            }
        });
        transactionList.addAll(uniqueTransactions);

        return transactionList;

    }


    public void undoPenaltyEvent(LoanAccountEntity loanAccountEntity, EventMasterEntity eventMasterEntity, String remarks) {
        Optional<PenaltyWindowEventEntity> penaltyWindowEventEntity = penaltyWindowEventRepository.findById(eventMasterEntity.getEventId());
        LocalDateTime time = LocalDateTime.now(Clock.systemUTC());
        final String user = UserContextHolder.checkedGetUser();
        //reverse penalty event
        if (penaltyWindowEventEntity.isPresent()) {
            if (penaltyWindowEventEntity.get().getPenaltyIntAccrEventType().equals(PenaltyIntAccrEventType.START)) {
                penaltyWindowEventEntity.get().setReversed(true);
                penaltyWindowEventEntity.get().setLastModifiedBy(user);
                penaltyWindowEventEntity.get().setLastModifiedOn(time);

                Optional<LoanPenaltyInterestHistoryEntity> loanPenaltyInterestHistoryEntityOptional =
                        loanPenaltyIntHistoryRepository.findByLoanIdAndStartEventId(loanAccountEntity.getIdentifier(),
                                penaltyWindowEventEntity.get().getId());
                loanAccountEntity.getLoanBalancesEntity().setPenaltyApplicationDueDate(null);
                loanAccountRepository.save(loanAccountEntity);
                if (loanPenaltyInterestHistoryEntityOptional.isPresent()) {
                    //delete history entry for undo start
                    loanPenaltyInterestHistoryEntityOptional.get().setDeleted(Boolean.TRUE);
                    loanPenaltyInterestHistoryEntityOptional.get().setLastModifiedBy(user);
                    loanPenaltyInterestHistoryEntityOptional.get().setLastModifiedOn(time);
                    loanPenaltyIntHistoryRepository.save(loanPenaltyInterestHistoryEntityOptional.get());
                    activityUtil.createPortfolioPayload(loanAccountEntity.getLoanAccountNumber(),
                            loanPenaltyInterestHistoryEntityOptional.get().getStartDate()
                                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), null, null,
                            loanPenaltyInterestHistoryEntityOptional.get().getPenaltyInterestRate().toString(),
                            null, null, null, EventConstants.UNDO_PENALTY_START,
                            EntityCategoryConstants.UNDO_START_PENALTY, EventType.UNDOPNLT.name(),
                            penaltyWindowEventEntity.get().getId(), null);
                }
            } else if (penaltyWindowEventEntity.get().getPenaltyIntAccrEventType().equals(PenaltyIntAccrEventType.STOP)) {
                BigDecimal penalty = new BigDecimal(0);
                //reverse event record
                penaltyWindowEventEntity.get().setReversed(true);
                penaltyWindowEventEntity.get().setLastModifiedBy(user);
                penaltyWindowEventEntity.get().setLastModifiedOn(time);
                //penaltyWindowEventRepository.save(penaltyWindowEventEntity.get());

                //update end date as null in history records
                Optional<LoanPenaltyInterestHistoryEntity> loanPenaltyInterestHistoryEntityOptional =
                        loanPenaltyIntHistoryRepository.findByLoanIdAndEndEventId(loanAccountEntity.getIdentifier(),
                                penaltyWindowEventEntity.get().getId());
                if (loanPenaltyInterestHistoryEntityOptional.isPresent()) {
                    //update penalty accrued upto date in LBT on undo stop
                    Optional<PenaltyInterestAccrualEntity> penaltyInterestAccrualEntityOptional =
                            penaltyIntAccrRepository.findTopByLoanIdAndPenaltyIntAccrDateLessThanEqualAndIsActiveTrueAndReversedFalseOrderByIdDesc(
                                    loanAccountEntity.getIdentifier(), loanPenaltyInterestHistoryEntityOptional.get().getEndDate());
                    loanAccountEntity.getLoanBalancesEntity().setPenaltyApplicationDueDate(
                            loanAccountEntity.getLoanBalancesEntity().getRollupDueDate());
                    if (penaltyInterestAccrualEntityOptional.isPresent()) {
                        LocalDate penaltyAccruedUpto = penaltyInterestAccrualEntityOptional.get().getPenaltyIntAccrDate();
                        loanAccountEntity.getLoanBalancesEntity().setPenaltyIntAccrUpto(penaltyAccruedUpto);
                        loanAccountEntity.setLastModifiedOn(time);
                        loanAccountEntity.setLastModifiedBy(user);
                        loanAccountRepository.save(loanAccountEntity);
                    }
                    //update history records
                    loanPenaltyInterestHistoryEntityOptional.get().setEndDate(null);
                    loanPenaltyInterestHistoryEntityOptional.get().setEndEventId(null);
                    loanPenaltyInterestHistoryEntityOptional.get().setLastModifiedBy(user);
                    loanPenaltyInterestHistoryEntityOptional.get().setLastModifiedOn(time);
                    loanPenaltyIntHistoryRepository.save(loanPenaltyInterestHistoryEntityOptional.get());

                    //reverse the demands created due to stop
                    List<DemandEntity> demandEntities = demandRepository.findByLoanIdAndEventTypeAndDemandTypeAndDemandDateAfterAndIsReversedFalse(
                            loanAccountEntity.getIdentifier(), EventType.PNLTYINT, DemandType.PENALTY,
                            loanPenaltyInterestHistoryEntityOptional.get().getStartDate());
                    List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                            .fetchAllDeferredDemandEntitiesForPenalty(loanAccountEntity.getIdentifier());
                    Map<Long,DeferredDemandEntity> mapByDemandID = Maps.newHashMap();
                    deferredDemandEntities.forEach(dd -> mapByDemandID.put(dd.getDemandId(),dd));

                    for (DemandEntity demandEntity : demandEntities) {
                        penalty = penalty.add(demandEntity.getDueAmount());
                        demandEntity.setReversed(Boolean.TRUE);
                        demandRepository.save(demandEntity);
                        if(mapByDemandID.containsKey(demandEntity.getId())){
                            mapByDemandID.get(demandEntity.getId()).setCurrentDeferredAmount(demandEntity.getDueAmount());
                        }
                    }
                    if(!mapByDemandID.isEmpty()){
                        deferDemandService.persist(Lists.newArrayList(mapByDemandID.values()));
                    }
                    activityUtil.createPortfolioPayloadForPenalty(loanAccountEntity.getLoanAccountNumber(),
                            penaltyWindowEventEntity.get().getEventDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                            null, null, loanPenaltyInterestHistoryEntityOptional.get()
                                    .getPenaltyInterestRate().toString(), penalty.toString(), null,
                            loanAccountEntity.getCurrencyCode(), null, EventConstants.UNDO_PENALTY_STOP,
                            EntityCategoryConstants.UNDO_STOP_PENALTY, EventType.UNDOPNLT.name(), penaltyWindowEventEntity.get().getId(), null);
                }

            }

            //create undo penalty event
            PenaltyWindowEventEntity undoPenaltyWindowEventEntity = PenaltyWindowEventEntity.createUndoPenaltyWindowEventEntity(
                    penaltyWindowEventEntity.get(), loanAccountEntity.getIdentifier(), remarks);
            PenaltyWindowEventEntity reversePenaltyWindowEvent = penaltyWindowEventRepository.save(undoPenaltyWindowEventEntity);
            penaltyWindowEventEntity.get().setReversalId(reversePenaltyWindowEvent.getId());
            penaltyWindowEventEntity.get().setLastModifiedBy(user);
            penaltyWindowEventEntity.get().setLastModifiedOn(time);
            penaltyWindowEventRepository.save(penaltyWindowEventEntity.get());
            eventMasterEntity.setReversed(Boolean.TRUE);
            eventMasterEntity.setReversalId(undoPenaltyWindowEventEntity.getId());
            eventMasterEntity.setLastModifiedOn(time);
            eventMasterEntity.setLastModifiedBy(user);
            eventMasterRepository.save(eventMasterEntity);
        }
    }

    public Map<CustomEntry, BigDecimal> getPenaltyHistory(Collection<Long> loanIds, List<Long> eventIds) {
        JPQLQuery penaltyQuery = extendedCustomRepositoryFactory.getJpqlQuery();

        QLoanPenaltyInterestHistoryEntity loanPenaltyInterestHistoryEntity = QLoanPenaltyInterestHistoryEntity.loanPenaltyInterestHistoryEntity;
        penaltyQuery.from(loanPenaltyInterestHistoryEntity)
                .where(loanPenaltyInterestHistoryEntity.loanId.in(loanIds)
                        .and(loanPenaltyInterestHistoryEntity.endEventId.in(eventIds))
                        .and(loanPenaltyInterestHistoryEntity.isDeleted.eq(Boolean.FALSE)));
        penaltyQuery.select(loanPenaltyInterestHistoryEntity.loanId
                , loanPenaltyInterestHistoryEntity.startDate
                , loanPenaltyInterestHistoryEntity.endDate
                , loanPenaltyInterestHistoryEntity.endEventId);

        List<Tuple> dataSet =
                extendedCustomRepositoryFactory
                        .getCustomRepository(LoanPenaltyInterestHistoryEntity.class)
                        .findAll(penaltyQuery);
        QDemandEntity demand = QDemandEntity.demandEntity;
        Map<CustomEntry, BigDecimal> amountMap = new HashMap<>();
        dataSet.stream().forEach(history -> {
            JPQLQuery demandQuery = extendedCustomRepositoryFactory.getJpqlQuery();
            demandQuery.from(demand).where(
                    demand.loanId.eq(history.get(loanPenaltyInterestHistoryEntity.loanId))
                            .and(demand.demandType.eq(DemandType.PENALTY)).and(demand.isReversed.eq(Boolean.FALSE))
                            .and(demand.demandDate.between(history.get(loanPenaltyInterestHistoryEntity.startDate),
                                    history.get(loanPenaltyInterestHistoryEntity.endDate)))
            );
            demandQuery.select(demand.dueAmount.sum());

            List demandA = extendedCustomRepositoryFactory
                    .getCustomRepository(DemandEntity.class)
                    .findAll(demandQuery);
            if (CollectionUtils.isNotEmpty(demandA) && demandA.get(0) != null) {
                amountMap.putIfAbsent(new CustomEntry(history.get(loanPenaltyInterestHistoryEntity.loanId),
                        history.get(loanPenaltyInterestHistoryEntity.endEventId)), (BigDecimal) demandA.get(0));
            }

        });
        return amountMap;
    }


    public Page<PenaltyHistory> getPenaltyData(Integer pageIndex, Integer recordSize, LocalDateTime modifiedInput) {

        try {
            List<PenaltyHistory> penaltyHistoryList = new ArrayList();
            final Pageable pageRequest = PageRequest.of(pageIndex, recordSize, Sort.Direction.ASC, "id");

            Page<Long> loanPenaltyInterestHistoryPage = loanAccountRepository.getModifiedLoanIdsFromDB(modifiedInput, pageRequest);

            for (Long loanId : loanPenaltyInterestHistoryPage.getContent()) {
                logger.info(loanId + "current loan id*************");
                PenaltyHistory penaltyHistory = getPenaltyHistory(loanId, true);
                penaltyHistoryList.add(penaltyHistory);

            }
            return new PageImpl(penaltyHistoryList, pageRequest, loanPenaltyInterestHistoryPage.getTotalElements());

        } catch (Exception exe) {
            logger.error(ExceptionConstants.EXCEPTION_OCCURRED, exe);
            if (exe.getMessage() == null)
                throw ServiceException.internalError("oops ! There is some exception while fetching record from database !");
            else
                throw ServiceException.internalError(exe.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PenaltyWriteOffAmount> getPenaltyDashboardDetails(final LoanAccount loanAccount, final String requestDate, boolean fetchOnlyDues, Long disbursementId,
                                                                  LoanMasterDTO loanMasterDTO) {
        DemandCollectionResultSet resultSet;
        if(Objects.isNull(disbursementId)) {
            resultSet = demandCollectionMappingService.filterByDemandTypeAndLoanId(DemandType.PENALTY, loanAccount.getIdentifier());
        }else{
            resultSet = demandCollectionMappingService.filterByDemandTypeAndLoanIdAndTrancheId(DemandType.PENALTY,loanAccount.getIdentifier(),disbursementId);
        }
        resultSet.filterUnReversedAndDeleted();

        List<PenaltyWriteOffAmount> dashboardDetails = Lists.newArrayList();
        for (Demand demand : resultSet.getDemandList()) {
            List<DemandCollection> filteredDC = resultSet.filterDemandCollectionForDemand(demand);
            BigDecimal paidAmount = BigDecimal.ZERO;
            BigDecimal writeOffAmount = BigDecimal.ZERO;
            BigDecimal capitalizedAmount = BigDecimal.ZERO;
            for (DemandCollection dc : filteredDC) {
                paidAmount = paidAmount.add(dc.getApportionedAndTransferredAmount());
                writeOffAmount = writeOffAmount.add(dc.getWaivedAmount());
                capitalizedAmount = capitalizedAmount.add(dc.getCapitalizedAmount());
            }

            PenaltyWriteOffAmount amount = new PenaltyWriteOffAmount();
            amount.setWrittenOff(writeOffAmount);
            amount.setTrancheId(demand.getTrancheId());
            amount.setPaidAmount(paidAmount);
            amount.setCapitalizedAmount(capitalizedAmount);
            amount.setDueAmount(demand.getDueAmount());
            amount.setDemandId(demand.getId());
            amount.setDueDate(demand.getDemandDate());
            amount.setRemainingAmount(amount.getDueAmount().subtract(paidAmount.add(writeOffAmount).add(capitalizedAmount)));
            dashboardDetails.add(amount);
        }

        Map<Long,BigDecimal> remainingDueTrancheWise = Maps.newHashMap();
        dashboardDetails.forEach(amount ->{
            if(!remainingDueTrancheWise.containsKey(amount.getTrancheId())){
                remainingDueTrancheWise.put(amount.getTrancheId(),amount.getRemainingAmount());
            }else{
                remainingDueTrancheWise.put(amount.getTrancheId(),
                        remainingDueTrancheWise.get(amount.getTrancheId()).add(amount.getRemainingAmount()));
            }
        });
        List<LoanPenaltyInterestHistoryEntity> penaltyInterestHistoryEntities = loanPenaltyIntHistoryRepository.findByLoanIdAndIsDeletedFalseOrderById(loanAccount.getIdentifier());
        if(Objects.isNull(loanMasterDTO)) {
            loanMasterDTO = getLoanMasterDTOForPenalty(loanAccount, penaltyInterestHistoryEntities);
        }

        List<DisbursementData> disbursementDatas;
        if(disbursementId != null){
            disbursementDatas = loanAccount.getDisbursementDatas()
                    .stream()
                    .filter(d -> d.getIdentifier().equals(disbursementId))
                    .collect(Collectors.toList());
        }else{
            disbursementDatas = loanAccount.getDisbursementDatas()
                    .stream()
                    .filter(d -> d.getStatus().equals(TrancheStatus.DISBRSED))
                    .collect(Collectors.toList());
        }

        List<RepaymentAmount> deferredDues = Lists.newArrayList();
        LoanMasterDTO finalLoanMasterDTO = loanMasterDTO;

        List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                .fetchAllNonZeroDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier());
        disbursementDatas.forEach(d -> fetchDeferredPenaltyDues(loanAccount,d.getIdentifier(),
                LocalDate.parse(requestDate), finalLoanMasterDTO,deferredDues,deferredDemandEntities));

        Map<Long, List<RepaymentAmount>> deferredDuesForTrancheMap = Maps.newHashMap();
        deferredDues.forEach(dd -> {
            deferredDuesForTrancheMap.putIfAbsent(dd.getTrancheId(), Lists.newArrayList());
            deferredDuesForTrancheMap.get(dd.getTrancheId()).add(dd);
        });

        disbursementDatas.forEach(d -> {
            BigDecimal penaltyAccruedDueNotPaid = loanAccountService.calculatePenaltyAccruedNotPaidForDisbursement(
                    loanAccount, DateConverter.dateFromIsoString(requestDate + "Z"),
                    null, finalLoanMasterDTO, Optional.of(penaltyInterestHistoryEntities), d.getIdentifier());
            List<RepaymentAmount> deferredDuesForTranche = deferredDuesForTrancheMap.getOrDefault(d.getIdentifier(),Lists.newArrayList());
            for(RepaymentAmount dd : deferredDuesForTranche){
                PenaltyWriteOffAmount amount = new PenaltyWriteOffAmount();
                amount.setTrancheId(d.getIdentifier());
                amount.setAccruedNotDueAmount(dd.getDueAmount());
                amount.setDueDate(requestDate);
                amount.setRemainingAmount(amount.getAccruedNotDueAmount());
                amount.setAccruedDue(true);
                amount.setDemandId(dd.getDemandId());
                amount.setDeferDemandId(dd.getDeferId());
                dashboardDetails.add(amount);
                penaltyAccruedDueNotPaid = penaltyAccruedDueNotPaid.subtract(dd.getDueAmount());
            }

            BigDecimal remainingAmount = remainingDueTrancheWise.getOrDefault(d.getIdentifier(),BigDecimal.ZERO);
            if (penaltyAccruedDueNotPaid.subtract(remainingAmount).compareTo(BigDecimal.ZERO) > 0) {
                PenaltyWriteOffAmount amount = new PenaltyWriteOffAmount();
                amount.setTrancheId(d.getIdentifier());
                amount.setAccruedNotDueAmount(penaltyAccruedDueNotPaid.subtract(remainingAmount));
                amount.setDueDate(requestDate);
                amount.setRemainingAmount(amount.getAccruedNotDueAmount());
                amount.setAccruedDue(true);
                dashboardDetails.add(amount);
            }
        });

        if (fetchOnlyDues) {
            return dashboardDetails.stream().filter(x ->
                    x.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        }
        return dashboardDetails;
    }

    public Map<Long, BigDecimal> apportionPenaltyAgainstDues(BigDecimal writeOffAmount, List<PenaltyWriteOffAmount> dues,
                                                             LoanMasterDTO loanMasterDTO) {
        Map<Long, BigDecimal> amountWrittenOffTrancheWise = Maps.newHashMap();
        Map<Long, DeferredDemandEntity> deferredDemandsMap = Maps.newHashMap();
        dues.forEach(due -> {
            if (Objects.nonNull(due.getDeferDemandId())) {
                DeferredDemandEntity deferredDemand = deferDemandService.findByDeferDemandId(due.getDeferDemandId())
                        .orElseThrow(() -> ServiceException.notFound("Defer demand Not Found For id {0}", due.getDeferDemandId()));
                Demand demand = loanMasterDTO.getDemandList().stream().filter(d -> d.getId().equals(deferredDemand.getDemandId())).findFirst()
                        .orElseThrow(() -> ServiceException.notFound("Demand Not Found For id {0}", deferredDemand.getDemandId()));
                due.setActualDueDate(demand.getDemandDate());
                deferredDemandsMap.put(deferredDemand.getId(), deferredDemand);
            }else {
                due.setActualDueDate(due.getDueDate());
            }
        });

        if (NumberUtils.isGreaterThanZero(writeOffAmount) && !dues.isEmpty()) {
            dues.sort(Comparator.comparing(due -> LocalDate.parse(due.getActualDueDate())));
            for (PenaltyWriteOffAmount due : dues) {
                if (!amountWrittenOffTrancheWise.containsKey(due.getTrancheId()))
                    amountWrittenOffTrancheWise.put(due.getTrancheId(), BigDecimal.ZERO);
                if (writeOffAmount.compareTo(due.getRemainingAmount()) >= 0) {
                    amountWrittenOffTrancheWise.put(due.getTrancheId(),
                            amountWrittenOffTrancheWise.get(due.getTrancheId()).add(due.getRemainingAmount()));
                    writeOffAmount = writeOffAmount.subtract(due.getRemainingAmount());
                    due.setWrittenOff(due.getWrittenOff().add(due.getRemainingAmount()));
                    if (Objects.nonNull(due.getDeferDemandId())) {
                        DeferredDemandEntity dd = deferredDemandsMap.get(due.getDeferDemandId());
                        dd.setCurrentDeferredAmount(dd.getCurrentDeferredAmount().subtract(due.getRemainingAmount()));
                        deferredDemandsMap.put(due.getDeferDemandId(), dd);
                    }
                    due.setRemainingAmount(BigDecimal.ZERO);
                } else {
                    amountWrittenOffTrancheWise.put(due.getTrancheId(),
                            amountWrittenOffTrancheWise.get(due.getTrancheId()).add(writeOffAmount));
                    due.setWrittenOff(due.getWrittenOff().add(writeOffAmount));
                    due.setRemainingAmount(due.getRemainingAmount().subtract(writeOffAmount));
                    if (Objects.nonNull(due.getDeferDemandId())) {
                        DeferredDemandEntity dd = deferredDemandsMap.get(due.getDeferDemandId());
                        dd.setCurrentDeferredAmount(dd.getCurrentDeferredAmount().subtract(writeOffAmount));
                        deferredDemandsMap.put(due.getDeferDemandId(), dd);
                    }
                    writeOffAmount = BigDecimal.ZERO;
                }
            }
        }
        deferDemandService.persist(Lists.newArrayList(deferredDemandsMap.values()));
        return amountWrittenOffTrancheWise;
    }

    public LoanTransactionEntity createTransactionEntryForWriteOff(PenaltyWriteOffDetailsEntity persistedRecord, LoanAccount loanAccount) {
        LoanProductEntity loanProductEntity = loanProductRepository.findByProductId(loanAccount.getProductId())
                .orElseThrow(() -> ServiceException.internalError(ExceptionConstants.UNABLE_TO_FIND_PRODUCT));

        return transactionService.saveTransaction(persistedRecord.getLoanId(), EventType.PNLTYWOF , persistedRecord.getIdentifier(), false,
                persistedRecord.getWriteOffDate(), persistedRecord.getWriteOffAmount(), loanAccount.getCurrencyCode() , null,
                loanProductEntity, null);
    }

    public void updateDemandCollectionPostWriteOff(PenaltyWriteOffDetailsEntity writeOffDetails, LoanAccount loanAccount,
                                                   List<PenaltyWriteOffAmount> penaltyDues) {
        CollectionEntity collectionEntity = CollectionEntity.createCollectionEntityForWaiver(writeOffDetails.getLoanId(),
                writeOffDetails.getIdentifier(), DateConverter.toIsoString(writeOffDetails.getWriteOffDate()),
                loanAccount.getCurrencyCode(), writeOffDetails.getWriteOffAmount(), false, EventType.PNLTYWOF);
        CollectionEntity persistedCollectionEntity = collectionRepository.save(collectionEntity);

        List<DemandCollectionEntity> dcToPersist = Lists.newArrayList();
        Map<Long,List<LoanScheduleModelRepaymentPeriod>> trancheWiseInstallments = trancheUtilService.getSortedTrancheWiseScheduleForLoan(
                Lists.newArrayList(loanAccount.getRepaymentPeriodInstallments()));

        for (PenaltyWriteOffAmount due : penaltyDues) {
            if (due.getWrittenOff().compareTo(BigDecimal.ZERO) > 0) {
                if (!due.isAccruedDue()) {
                    DemandEntity demandEntity = demandRepository.findById(due.getDemandId())
                            .orElseThrow(()->ServiceException.internalError("DemandEntity not found"));
                    List<DemandCollectionEntity> dcForDemand = demandCollectionRepository.findByDemandEntity(demandEntity);
                    BigDecimal collectedForDemand = dcForDemand.stream().filter(x -> !x.getReversed()).map(x -> x.getApportionedAndTransferredAmount()
                            .add(x.getWaivedAmount()).add(x.getCapitalizedAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal finalWrittenOffAmount = due.getTotalPaid().subtract(collectedForDemand);
                    DemandCollectionEntity dc = DemandCollectionEntity.createDemandCollectionEntity(
                            writeOffDetails.getLoanId(), persistedCollectionEntity, demandEntity, BigDecimal.ZERO,
                            finalWrittenOffAmount, loanAccount.getCurrencyCode(), BigDecimal.ZERO);
                    dcToPersist.add(dc);
                } else {
                    List<LoanScheduleModelRepaymentPeriod> trancheInstallments = trancheWiseInstallments.get(due.getTrancheId());
                    LoanScheduleModelRepaymentPeriod nextInstallment = trancheInstallments
                            .stream().filter(x -> !x.isDummyScheduleEntry() && !LocalDate.parse(x.getInstallmentDate()).isBefore(LocalDate.parse(due.getDueDate()))).findFirst()
                            .orElse(trancheInstallments.get(trancheInstallments.size() - 1));
                    DemandEntity demandEntity = DemandEntity.createDemandEntity(writeOffDetails.getLoanId(), due.getTrancheId(),
                            EventType.PNLTYWOF, writeOffDetails.getIdentifier(), null, null, (long) nextInstallment.getPeriodNumber(),
                            DateConverter.dateFromIsoString(due.getDueDate() + "Z"), BigDecimal.ZERO,
                            due.getWrittenOff(), loanAccount.getCurrencyCode(), "Write-Off");
                    DemandEntity persistedDemandEntity = demandRepository.save(demandEntity);
                    DemandCollectionEntity dc = DemandCollectionEntity.createDemandCollectionEntity(
                            writeOffDetails.getLoanId(), persistedCollectionEntity, persistedDemandEntity, BigDecimal.ZERO,
                            due.getWrittenOff(), loanAccount.getCurrencyCode(), BigDecimal.ZERO);
                    dcToPersist.add(dc);
                }
            }
        }
        demandCollectionRepository.saveAll(dcToPersist);
    }

    public void reversePenaltyWriteoff(LoanTransactionEntity loanTransactionEntity, EventMasterEntity eventMasterEntity) {
        PenaltyWriteOffDetailsEntity eventRecord = penaltyWriteOffDetailsRepository.findByidentifier(loanTransactionEntity.getEventId())
                .orElseThrow(()->ServiceException.internalError("unable to find PenaltyWriteOffDetailsEntity"));
        PenaltyWriteOffDetailsEntity reversalEntry = penaltyWriteOffDetailsRepository.save(PenaltyMapper.mapForWriteOffUndo(eventRecord));
        eventRecord.setReversed(true);
        eventRecord.setReversalId(reversalEntry.getIdentifier());
        penaltyWriteOffDetailsRepository.save(eventRecord);

        eventMasterEntity.setReversed(true);
        eventMasterEntity.setReversalId(reversalEntry.getIdentifier());
        final LocalDateTime updatedTime = LocalDateTime.now(Clock.systemUTC());
        final String user = UserContextHolder.checkedGetUser();
        eventMasterEntity.setLastModifiedOn(updatedTime);
        eventMasterEntity.setLastModifiedBy(user);
        eventMasterRepository.save(eventMasterEntity);

        genericLoanServices.reverseTransactionEntry(loanTransactionEntity, reversalEntry.getIdentifier(), EventType.UNDOPNWO);

        List<CollectionEntity> collectionEntities = collectionRepository.findByEventTypeAndEventId(
                EventType.PNLTYWOF, eventRecord.getIdentifier());
        List<CollectionEntity> collectionsToReverse = Lists.newArrayList();
        List<DemandCollectionEntity> demandCollectionsToReverse = Lists.newArrayList();
        List<DemandEntity> demandsToReverse = Lists.newArrayList();
        for (CollectionEntity c : collectionEntities) {
            c.setReversed(true);
            c.setLastModifiedOn(updatedTime);
            c.setLastModifiedBy(user);
            collectionsToReverse.add(c);
            List<DemandCollectionEntity> dcs = demandCollectionRepository.findByCollectionEntity(c);
            for (DemandCollectionEntity dc : dcs) {
                dc.setReversed(true);
                dc.setLastModifiedOn(updatedTime);
                dc.setLastModifiedBy(user);
                demandCollectionsToReverse.add(dc);
                if (dc.getDemandEntity().getEventType() == EventType.PNLTYWOF) {
                    dc.getDemandEntity().setReversed(true);
                    dc.getDemandEntity().setLastModifiedOn(updatedTime);
                    dc.getDemandEntity().setLastModifiedBy(user);
                    demandsToReverse.add(dc.getDemandEntity());
                }
            }
        }
        collectionRepository.saveAll(collectionsToReverse);
        demandRepository.saveAll(demandsToReverse);
        demandCollectionRepository.saveAll(demandCollectionsToReverse);

        updateDeferredDemands(loanTransactionEntity.getLoanId(),
                loanTransactionEntity.getTransactionDate(),loanTransactionEntity.getTransactionAmount());

        LoanAccountEntity loanAccountEntity = loanAccountService.findById(loanTransactionEntity.getLoanId());
        for (DemandCollectionEntity dc : demandCollectionsToReverse) {
            BigDecimal writtenOff = dc.getApportionedAndTransferredAmount().add(dc.getCapitalizedAmount()).add(dc.getWaivedAmount());
            for (DisbursementDataEntity tranche : loanAccountEntity.getDisbursementDataEntitySet()) {
                if (dc.getDemandEntity().getTrancheId().equals(tranche.getId())) {
                    tranche.getTrancheBalancesEntity().setTotalPenaltyPaid(
                            tranche.getTrancheBalancesEntity().getTotalPenaltyPaid().subtract(writtenOff));
                }
            }
            loanAccountEntity.getLoanBalancesEntity().setTotalPenaltyPaid(
                    loanAccountEntity.getLoanBalancesEntity().getTotalPenaltyPaid().subtract(writtenOff));
        }
        loanAccountEntity.getLoanBalancesEntity().setHash(hashService.createHash(loanAccountEntity.getLoanBalancesEntity()));
        loanAccountRepository.save(loanAccountEntity);
        EventContextHolder.setEventContext(EventType.UNDOPNWO.name(), reversalEntry.getIdentifier());
    }

    private void updateDeferredDemands(Long loanId, LocalDate eventDate, BigDecimal writeOffAmount) {
        List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                .fetchAllDeferredDemandEntitiesForPenalty(loanId)
                .stream()
                .filter(x -> x.nextDeferDate().isAfter(eventDate))
                .collect(Collectors.toList());
        if (!deferredDemandEntities.isEmpty()) {
            List<Long> demandIds = deferredDemandEntities.stream().map(DeferredDemandEntity::getDemandId).collect(Collectors.toList());
            List<DemandEntity> demandEntities = demandRepository.findByIdIn(demandIds);
            @Data
            @AllArgsConstructor
            class DeferDemandPointer {
                private LocalDate dueDate;
                private DeferredDemandEntity deferredDemandEntity;
            }
            List<DeferDemandPointer> pointers = Lists.newArrayList();

            deferredDemandEntities.forEach(dd -> {
                DemandEntity d = demandEntities.stream().filter(x -> x.getId().equals(dd.getDemandId())).findFirst()
                        .orElseThrow(() -> ServiceException.notFound("Demand Not Found : {0}", dd.getDemandId()));
                pointers.add(new DeferDemandPointer(d.getDemandDate(), dd));
            });
            pointers.sort(Comparator.comparing(DeferDemandPointer::getDueDate));
            for (DeferDemandPointer p : pointers) {
                if (NumberUtils.isGreaterThanZero(writeOffAmount)) {
                    if (writeOffAmount.compareTo(p.getDeferredDemandEntity().getOriginalDeferredAmount()) >= 0) {
                        p.getDeferredDemandEntity().setCurrentDeferredAmount(p.getDeferredDemandEntity().getOriginalDeferredAmount());
                        writeOffAmount = writeOffAmount.subtract(p.getDeferredDemandEntity().getOriginalDeferredAmount());
                    } else {
                        p.getDeferredDemandEntity().setCurrentDeferredAmount(p.getDeferredDemandEntity().getCurrentDeferredAmount().add(writeOffAmount));
                        writeOffAmount = BigDecimal.ZERO;
                    }
                }
            }
            deferDemandService.persist(pointers.stream().map(DeferDemandPointer::getDeferredDemandEntity).collect(Collectors.toList()));
        }
    }

    public void updateLoanAndTrancheBalances(Long loanId, Map<Long, BigDecimal> penaltyApportionedMap) {
        LoanAccountEntity loanAccount = loanAccountService.findById(loanId);
        for (DisbursementDataEntity d : loanAccount.getDisbursementDataEntitySet()) {
            BigDecimal totalWaived = penaltyApportionedMap.get(d.getId());
            if (totalWaived != null) {
                d.getTrancheBalancesEntity().setTotalPenaltyPaid(d.getTrancheBalancesEntity().getTotalPenaltyPaid().add(totalWaived));
                loanAccount.getLoanBalancesEntity().setTotalPenaltyPaid(loanAccount.getLoanBalancesEntity().getTotalPenaltyPaid().add(totalWaived));
            }
        }
        loanAccount.setLastModifiedOn(LocalDateTime.now(Clock.systemUTC()));
        loanAccount.setLastModifiedBy(UserContextHolder.checkedGetUser());
        loanAccount.getLoanBalancesEntity().setHash(hashService.createHash(loanAccount.getLoanBalancesEntity()));
        loanAccountRepository.save(loanAccount);
    }

    public void createEventMasterAndGLForWriteOff(PenaltyWriteOffDetailsEntity persistedRecord, LoanAccount loanAccount,
                                                  LoanTransactionEntity persistedTransactionEntry, PenaltyWriteOffCommand command) {
        EventMaster eventMaster = new EventMaster(persistedRecord.getLoanId(), EventType.PNLTYWOF, persistedRecord.getIdentifier(),
                persistedRecord.getWriteOffDate().toString(), false);
        EventMasterEntity eventMasterEntity = EventMasterMapper.map(eventMaster);
        eventMasterRepository.save(eventMasterEntity);

        JournalEntry journalEntry = generateGLForWriteOff(loanAccount, persistedRecord, persistedTransactionEntry.getId());
        createJournalEntry(persistedTransactionEntry, journalEntry, PENALTY_WRITEOFF_MESSAGE, command);

    }

    public PenaltyWriteOffDetails getWriteOffEventDetails(Long eventId) {
        PenaltyWriteOffDetailsEntity writeOffDetailsEntity = penaltyWriteOffDetailsRepository.findByidentifier(eventId)
                .orElseThrow(()->ServiceException.internalError("unable to find PenaltyWriteOffDetailsEntity"));
        PenaltyWriteOffDetails writeOffDetails = new PenaltyWriteOffDetails();
        writeOffDetails.setLoanId(writeOffDetailsEntity.getLoanId());
        writeOffDetails.setWriteOffDate(writeOffDetailsEntity.getWriteOffDate().toString());
        writeOffDetails.setTotalDue(writeOffDetailsEntity.getTotalDue());
        writeOffDetails.setTotalWriteOff(writeOffDetailsEntity.getWriteOffAmount());
        writeOffDetails.setRemarks(writeOffDetailsEntity.getRemarks());
        writeOffDetails.setCreatedBy(writeOffDetailsEntity.getCreatedBy());
        writeOffDetails.setCreatedOn(writeOffDetailsEntity.getCreatedOn().toString());
        return writeOffDetails;
    }

    public LoanPenaltyInterestHistoryEntity getLastStartEventForLoan(long loanId) {
        List<LoanPenaltyInterestHistoryEntity> historyEntities = loanPenaltyIntHistoryRepository.findByLoanIdAndIsDeletedFalseOrderById(loanId);
        return historyEntities.get(historyEntities.size() - 1);
    }

    public PenaltyStartStopReverseDetails getPenaltyStartStopReversalDetails(Long eventId) {

        PenaltyStartStopReverseDetails penaltyStartStopReverseDetails = new PenaltyStartStopReverseDetails();
        Optional<PenaltyWindowEventEntity> penaltyWindowEventEntity = penaltyWindowEventRepository.findById(eventId);
        if (penaltyWindowEventEntity.isPresent()) {
            PenaltyWindowEventEntity penaltyWindowEvent = penaltyWindowEventEntity.get();
            penaltyStartStopReverseDetails.setId(penaltyWindowEvent.getId());
            penaltyStartStopReverseDetails.setLoanId(penaltyWindowEvent.getLoanId());
            penaltyStartStopReverseDetails.setPenaltyInterestRate(penaltyWindowEvent.getPenaltyInterestRate());
            penaltyStartStopReverseDetails.setPenalCalculationInterestMethod(StringUtils.toString(penaltyWindowEvent.getPenaltyCalculationMethod()));
            penaltyStartStopReverseDetails.setPenaltyIntAccrEventType(StringUtils.toString(penaltyWindowEvent.getPenaltyIntAccrEventType()));
            penaltyStartStopReverseDetails.setEventDate(StringUtils.toString(penaltyWindowEvent.getEventDate()));
            penaltyStartStopReverseDetails.setRemarks(StringUtils.toString(penaltyWindowEvent.getRemarks()));
            penaltyStartStopReverseDetails.setCreatedOn(StringUtils.toString(penaltyWindowEvent.getCreatedOn()));
            penaltyStartStopReverseDetails.setLastModifiedOn(StringUtils.toString(penaltyWindowEvent.getLastModifiedOn()));

        } else {
            throw ServiceException.badRequest("No Penalty Start/Stop Reverse Event Found");
        }
        return penaltyStartStopReverseDetails;
    }

    public void penaltyApplication(PenaltyApplicationCommand penaltyApplicationCommand, LoanAccountEntity loanAccountEntity) {
        LocalDate runDate = penaltyApplicationCommand.getPenaltyApplicationDate();
        LoanProductEntity loanProductEntity = loanProductRepository.findByProductId(loanAccountEntity.getProductIdentifier())
                .orElseThrow(() -> ServiceException.internalError(ExceptionConstants.UNABLE_TO_FIND_PRODUCT));
        LoanProduct loanProduct = LoanProductMapper.map(loanProductEntity);
        List<LoanPenaltyInterestHistoryEntity> loanPenaltyInterestHistoryEntities =
                loanPenaltyIntHistoryRepository.findByLoanIdAndIsDeletedFalseOrderById(loanAccountEntity.getIdentifier());
        if (loanPenaltyInterestHistoryEntities.isEmpty()) return;

        LoanScheduleModelRepaymentPeriodEntity installmentBefore = loanAccountEntity
                .getRepaymentPeriodInstallments()
                .stream()
                .filter(x -> (x.getPrincipalPayingIpd() || x.getInterestPayingIpd()) && !x.getInstallmentDate().isAfter(runDate))
                .reduce((first,second) -> second)
                .orElseThrow(() -> ServiceException.badRequest("No Applicable IPD Found For Application Date {0} !",runDate));
        LocalDate applicationDate = installmentBefore.getInstallmentDate();

        transactionChecker.valueDateChecker(applicationDate.toString(), loanAccountEntity.getIdentifier(), null);
        transactionChecker.transactionValidationBasedOnEvents(loanAccountEntity, applicationDate.toString(),
                EventType.PENROLP);

        List<RepaymentAmount> penaltyDues = Lists.newArrayList();
        LoanAccount loanAccount = LoanAccountMapper.map(loanAccountEntity);
        loanAccountEntity.getDisbursementDataEntitySet().forEach(disbursementData -> penaltyDues.addAll(
                getPenaltyDues(disbursementData.getId(), applicationDate, loanAccount, null,
                        null, loanPenaltyInterestHistoryEntities)));
        LoanScheduleModelRepaymentPeriodEntity installmentOnApplicationDate = loanAccountEntity
                .getRepaymentPeriodInstallments()
                .stream()
                .filter(x -> x.getInstallmentDate().isEqual(applicationDate))
                .findFirst()
                .orElseThrow(() -> ServiceException.notFound("No Installment Found For IPD {0} !", applicationDate));
        boolean isDueForRollup = checkIfPenaltyDueForRollup(loanAccount, installmentOnApplicationDate);
        List<RepaymentAmount> penaltyDuesWithoutDemand;
        List<DeferredDemandEntity> penaltyDeferredDemands = deferDemandService
                .fetchAllDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier())
                .stream()
                .filter(x -> !x.nextDeferDate().isAfter(runDate))
                .collect(Collectors.toList());

        penaltyDuesWithoutDemand = penaltyDues
                .stream()
                .filter(x -> Objects.isNull(x.getDemandId()))
                .collect(Collectors.toList());

        penaltyDeferredDemands.forEach(d -> {
            Optional<DemandEntity> demand = demandRepository.findById(d.getDemandId());
            demand.ifPresent(x->{
                RepaymentAmount penaltyGenerated = generatePenaltyAmount(null, applicationDate.toString(),
                        installmentBefore.getPeriodNumber(), d.getCurrentDeferredAmount(), x.getTrancheId(), loanProduct);
                if (Objects.nonNull(penaltyGenerated))
                    penaltyDuesWithoutDemand.add(penaltyGenerated);
            });
        });
        if (isDueForRollup){
            List<RepaymentAmount> dueForRollUps = getPendingPenaltyDueForRollUp(loanAccountEntity, applicationDate, penaltyDues);
            penaltyDuesWithoutDemand.addAll(dueForRollUps);

        }

        BigDecimal totalPenaltyDue = penaltyDuesWithoutDemand
                .stream()
                .map(RepaymentAmount::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        String remarks = "Penalty Application";
        SystemEventEntity newSystemEvent = SystemEventEntity.createSystemEventEntry(loanAccount.getIdentifier(),
                isDueForRollup ? EventType.PENROLP : EventType.PNLTYAPP, applicationDate, applicationDate, totalPenaltyDue, false, remarks);
        SystemEventEntity persistedSystemEvent = systemEventRepository.save(newSystemEvent);
        if(loanAccountEntity.getLoanCategory()==LoanCategory.SYNDCATE && persistedSystemEvent!=null){

            loanEventSequenceService.checkForEventProceed(persistedSystemEvent.getEventType(),loanAccount,persistedSystemEvent.getAmount(),persistedSystemEvent.getDate());
            loanEventSequenceService.saveLoanEvent(persistedSystemEvent.getEventType(),persistedSystemEvent.getId(),
                    loanAccount,persistedSystemEvent.getAmount(),persistedSystemEvent.getDate());
        }
        if (isDueForRollup && CollectionUtils.isNotEmpty(penaltyDuesWithoutDemand)) {
            loanArchiveService.archive(loanAccount.getIdentifier(), EventType.PENROLP, persistedSystemEvent.getId());
            penaltyRollup(applicationDate,loanAccount, penaltyDuesWithoutDemand);
            BigDecimal totalPenaltyPaidForLBT = loanAccount.getLoanBalances().getTotalPenaltyPaid().add(totalPenaltyDue);
            loanAccount.setTotalPenaltyPaid(totalPenaltyPaidForLBT);
        }
        EventMaster eventMaster = new EventMaster(loanAccount.getIdentifier(), isDueForRollup ?
                EventType.PENROLP : EventType.PNLTYAPP, persistedSystemEvent.getId(), applicationDate.toString(), false);
        EventMasterEntity eventMasterEntity = EventMasterMapper.map(eventMaster);
        eventMasterRepository.save(eventMasterEntity);
        if (isDueForRollup) {
            LoanTransactionEntity persistedTxn = transactionService.saveTransaction(loanAccount.getIdentifier(),
                    EventType.PENROLP, persistedSystemEvent.getId(), true, applicationDate,
                    totalPenaltyDue, loanAccount.getCurrencyCode(), null);
            OBSAmount obsAmount;
            //changes for loan financing
            if(loanAccountEntity.getLoanCategory().equals(LoanCategory.LFCHLN)) {
                obsAmount =loanFinanceUtil.updateDrawnBalance(persistedTxn.getTransactionDate().toString(),
                        loanAccountEntity,totalPenaltyDue,persistedSystemEvent.getId(),EventType.PENROLP,BigDecimal.ZERO);
            }
            else{
                obsAmount = loanDrawnBalancesService.updateLoanDrawnBalances(loanAccountEntity,totalPenaltyDue,
                        persistedSystemEvent.getId(),EventType.PENROLP,persistedTxn.getTransactionDate());
            }
            loanAccount.getLoanBalances().setUndrawnBalance(loanAccount.getLoanBalances()
                    .getUndrawnBalance().subtract(totalPenaltyDue));
            loanAccount.setLoanDrawnBalances(LoanAccountMapper.mapDrawnBalance(loanAccount,loanAccountEntity));
            JournalEntry journalEntry = generateGLForPenaltyRollup(loanAccount, totalPenaltyDue,
                    applicationDate.toString(), persistedTxn.getId(),loanProductEntity,obsAmount);
            createJournalEntry(persistedTxn, journalEntry, PENALTY_ROLLUP_MESSAGE, penaltyApplicationCommand);
        }
        createDemandsForPenaltyApplication(loanAccount, penaltyDuesWithoutDemand,penaltyDeferredDemands,
                persistedSystemEvent.getId(), isDueForRollup,applicationDate);
        activityUtil.createPortfolioPayload(loanAccountEntity.getLoanAccountNumber(),
                LocalDateTime.now(Clock.systemUTC()).toString(), null, null,
                persistedSystemEvent.getAmount().setScale(2, RoundingMode.HALF_UP).toString(),
                null, loanAccountEntity.getCurrencyCode(), null, EventConstants.PENALTY_APPLICATION,
                EntityCategoryConstants.PENALTY, Optional.ofNullable(persistedSystemEvent.getEventType())
                        .map(EventType::name).orElse(null), persistedSystemEvent.getId(),
                persistedSystemEvent.getDate().toString());
        loanAccount.setPenaltyAppliedUpto(applicationDate.toString());
        if(!isPenaltyApplicationDue(loanAccount.getIdentifier())){
            loanAccount.setPenaltyApplicationDueDate(null);
        }else {
            loanAccount.setPenaltyApplicationDueDate(Objects.isNull(loanAccountEntity.getLoanBalancesEntity().getRollupDueDate())
                    ? null : loanAccountEntity.getLoanBalancesEntity().getRollupDueDate().toString());
        }
        loanAccountEntity.getLoanBalancesEntity().setHash(hashService.createHash(loanAccountEntity.getLoanBalancesEntity()));
        loanAccountRepository.save(LoanAccountMapper.mapOverOldEntityForRepayment(loanAccount,loanAccountEntity));
    }

    public void penaltyRollup(LocalDate rollupDate, LoanAccount loanAccount, List<RepaymentAmount> penaltyDues) {
        BigDecimal totalDueForCapitalization = penaltyDues
                .stream()
                .map(RepaymentAmount::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, BigDecimal> trancheWisePenaltyDueMap = Maps.newHashMap();
        for (RepaymentAmount due : penaltyDues) {
            trancheWisePenaltyDueMap.putIfAbsent(due.getTrancheId(),BigDecimal.ZERO);
            trancheWisePenaltyDueMap.put(due.getTrancheId(), trancheWisePenaltyDueMap.get(due.getTrancheId()).add(due.getDueAmount()));
        }

        genericLoanServices.rescheduleLoanForFeeCapitalisation(loanAccount, rollupDate.toString(),
                totalDueForCapitalization, EventType.MINTCAP, null, Boolean.FALSE, Boolean.FALSE, trancheWisePenaltyDueMap,
                null, Maps.newHashMap(), Optional.empty(), null,
                null, Maps.newHashMap(), null, null, false);

        Map<Long,DisbursementData> disbursementDataMap = Maps.newHashMap();
        loanAccount.getDisbursementDatas().forEach(x -> disbursementDataMap.put(x.getIdentifier(),x));
        trancheWisePenaltyDueMap.forEach((k,v) -> disbursementDataMap.get(k).getTrancheBalances().setPrincipalBalance(
                disbursementDataMap.get(k).getTrancheBalances().getPrincipalBalance().add(v)));

        trancheWisePenaltyDueMap.forEach((k,v) ->{
            Optional<LoanScheduleModelRepaymentPeriod> installment = loanAccount
                    .getRepaymentPeriodInstallments()
                    .stream()
                    .filter(x -> x.getTrancheSequenceNumber().equals(disbursementDataMap.get(k).getDisbursementSequenceNumber())
                            && LocalDate.parse(x.getInstallmentDate()).isEqual(rollupDate))
                    .findAny();
            if(installment.isPresent()){
                installment.get().setPenaltyCapitalised(trancheWisePenaltyDueMap.get(k));
                installment.get().setManuallyCapitalizedInterest(installment.get().getManuallyCapitalizedInterest()
                        .subtract(trancheWisePenaltyDueMap.get(k)));
            }
        });
    }

    public Optional<SystemEventEntity> undoPenaltyApplication(EventMasterEntity eventMasterEntity, String remarks,
                                                              boolean penaltyRollupFlag) {
        SystemEventEntity systemEventEntity =
                this.systemEventRepository.findByIdAndEventTypeAndLoanId(eventMasterEntity.getEventId(), eventMasterEntity.getEventType(),eventMasterEntity.getLoanId()).orElseThrow(()->ServiceException.notFound(ExceptionConstants.UNABLE_TO_FIND_SYSTEM_EVENT));


        undo(eventMasterEntity, remarks, systemEventEntity, penaltyRollupFlag);
        return Optional.of(systemEventEntity);
    }

    public boolean checkIfPenaltyApplicationPending(LoanAccountEntity loanAccountEntity, LocalDate transactionDate) {
        Optional<LoanPenaltyInterestHistoryEntity> latestPenaltyHistory = loanPenaltyIntHistoryRepository
                .findTopByLoanIdAndEndDateIsNullAndIsDeletedFalseOrderByIdDesc(loanAccountEntity.getIdentifier());
        if(!latestPenaltyHistory.isPresent())return false;
        if(Objects.nonNull(latestPenaltyHistory.get().getEndDate()))return false;
        Optional<LoanBalancesEntity> loanBalancesEntity = loanBalancesRepository.findByLoanId(loanAccountEntity.getIdentifier());
        if (!loanBalancesEntity.isPresent() || Objects.isNull(loanBalancesEntity.get().getPenaltyApplicationDueDate())) return false;
        return !transactionDate.isBefore(loanBalancesEntity.get().getPenaltyApplicationDueDate());
    }

    private void undo(EventMasterEntity eventMasterEntity, String remarks, SystemEventEntity systemEventEntity,
                      boolean penaltyRollupFlag) {
        final LocalDateTime time = LocalDateTime.now(Clock.systemUTC());
        final String user = UserContextHolder.checkedGetUser();
        //undo system event data
        SystemEventEntity systemEventEntityUndo = SystemEventEntity.undoSystemEvent(systemEventEntity, penaltyRollupFlag ?
                EventType.UNPNROLP : EventType.UNDOPAPP, remarks);
        this.systemEventRepository.save(systemEventEntityUndo);

        // adding event context holder
        EventContextHolder.setEventContext(systemEventEntityUndo.getEventType().name(), systemEventEntityUndo.getId());
        systemEventEntity.setRerunId((long) -1);
        systemEventEntity.setReversalId(systemEventEntityUndo.getId());

        //undo of master event data
        eventMasterEntity.setReversed(true);
        eventMasterEntity.setReversalId(systemEventEntityUndo.getId());
        eventMasterEntity.setLastModifiedOn(time);
        this.eventMasterRepository.save(eventMasterEntity);

        LoanAccountEntity loanAccountEntity = this.loanAccountRepository.findByIdentifier(eventMasterEntity.getLoanId())
                .orElseThrow(() -> ServiceException.notFound("Loan Account with Id {0} Not Found !", eventMasterEntity.getLoanId()));

        //undo of demand
        List<DemandEntity> demandEntities = demandRepository.findByEventTypeAndEventId(penaltyRollupFlag ?
                EventType.PENROLP : EventType.PNLTYAPP, systemEventEntity.getId());
        if (penaltyRollupFlag) {
            undoForPenaltyRollup(loanAccountEntity, systemEventEntity, systemEventEntityUndo, eventMasterEntity);

        } else {
            for (DemandEntity demandEntity : demandEntities) {
                demandEntity.setReversed(true);
                demandEntity.setLastModifiedOn(time);
                demandEntity.setLastModifiedBy(user);
            }
            this.demandRepository.saveAll(demandEntities);

        }
        List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                .fetchAllDeferredDemandEntitiesForPenalty(loanAccountEntity.getIdentifier());
        deferredDemandEntities.forEach(dd -> {
            if(NumberUtils.isZero(dd.getCurrentDeferredAmount()))
                dd.setCurrentDeferredAmount(dd.getOriginalDeferredAmount());
        });
        this.deferDemandService.persist(deferredDemandEntities);

        loanAccountEntity.getLoanBalancesEntity().setPenaltyApplicationDueDate(eventMasterEntity.getEventDate());
        Optional<EventMasterEntity> lastUnreversedEvent = eventMasterRepository
                .findTopByLoanIdAndEventTypeInAndReversedFalseOrderByEventDateDesc(eventMasterEntity.getLoanId(), Arrays.asList(EventType.PENROLP, EventType.PNLTYAPP));
        loanAccountEntity.getLoanBalancesEntity().setPenaltyAppliedUpto(
                lastUnreversedEvent.map(EventMasterEntity::getEventDate).orElse(null));
        loanAccountEntity.getLoanBalancesEntity().setHash(hashService.createHash(loanAccountEntity.getLoanBalancesEntity()));
        loanAccountRepository.save(loanAccountEntity);
    }

    private void undoForPenaltyRollup(LoanAccountEntity loanAccountEntity, SystemEventEntity systemEventEntity,
                                      SystemEventEntity systemEventEntityUndo, EventMasterEntity eventMasterEntity) {
        LoanTransactionEntity loanTransaction = loanTransactionEntityRepository.findByEventTypeAndEventId(
                        EventType.PENROLP, systemEventEntity.getId())
                .orElseThrow(() -> ServiceException.notFound(
                        "Loan Transaction Not Found For event Id : {0}", systemEventEntity.getId()));
        loanArchiveService.restoreArchive(eventMasterEntity.getLoanId(), EventType.PENROLP, eventMasterEntity.getEventId());
        EventContextHolder.setEventContext(EventType.UNPNROLP.name(), systemEventEntityUndo.getId());
        genericLoanServices.reverseTransactionEntry(loanTransaction, systemEventEntityUndo.getId(), EventType.UNPNROLP);
        Map<Long, DisbursementDataEntity> disbursementDataEntityMap = Maps.newHashMap();
        for (DisbursementDataEntity disbursementDataEntity : loanAccountEntity.getDisbursementDataEntitySet()) {
            disbursementDataEntityMap.put(disbursementDataEntity.getId(), disbursementDataEntity);
        }
        List<CollectionEntity> collections = collectionRepository.findByEventTypeAndEventId(EventType.PENROLP,systemEventEntity.getId());
        List<Long> collectionIds = collections.stream().map(CollectionEntity::getId).collect(Collectors.toList());
        List<DemandCollectionEntity> demandCollections = demandCollectionRepository.findByCollectionIds(collectionIds);
        demandCollections.forEach(dc -> {
            DisbursementDataEntity disbursementDataEntity = disbursementDataEntityMap.get(dc.getDemandEntity().getTrancheId());
            disbursementDataEntity.getTrancheBalancesEntity().setPrincipalBalance(
                    disbursementDataEntity.getTrancheBalancesEntity().getPrincipalBalance().subtract(dc.getCapitalizedAmount()));
            loanAccountEntity.getLoanBalancesEntity().setUndrawnBalance(loanAccountEntity.getLoanBalancesEntity().getUndrawnBalance().add(dc.getCapitalizedAmount()));
            disbursementDataEntity.getTrancheBalancesEntity().setHash(hashService.createHash(disbursementDataEntity.getTrancheBalancesEntity()));
            dc.setReversed(true);
            if(dc.getDemandEntity().getEventType().equals(EventType.PENROLP)) {
                dc.getDemandEntity().setReversed(true);
            }
            dc.getCollectionEntity().setReversed(true);
            loanAccountEntity.getLoanBalancesEntity().setTotalPenaltyPaid(
                    loanAccountEntity.getLoanBalancesEntity().getTotalPenaltyPaid().subtract(dc.getCapitalizedAmount()));
        });
        demandCollectionRepository.saveAll(demandCollections);
    }

    private JournalEntry generateGLForWriteOff(LoanAccount loanAccount, PenaltyWriteOffDetailsEntity persistedRecord, Long transactionId) {
        LoanProductEntity loanProductEntity = loanProductRepository.findByProductId(loanAccount.getProductId())
                .orElseThrow(() -> ServiceException.internalError(ExceptionConstants.UNABLE_TO_FIND_PRODUCT));
        JournalEntry journalEntry = journalEntryHelper.getJournalEntryForPenaltyWriteOff(persistedRecord, loanProductEntity);
        updateParamsForJournalEntryBeforePersist(journalEntry, transactionId, loanAccount,
                persistedRecord.getWriteOffDate().toString(), PENALTY_WRITEOFF_MESSAGE, PENALTY_WRITEOFF_MESSAGE);
        return journalEntry;
    }

    private JournalEntry generateGLForPenaltyRollup(LoanAccount loanAccount, BigDecimal rollupAmount,String rollupDueDate,
                                                    Long transactionId,LoanProductEntity loanProductEntity,OBSAmount obsAmount) {
        JournalEntry journalEntry = journalEntryHelper.getJournalEntryForPenaltyRollup(rollupAmount, loanProductEntity);
        if(obsAmount !=null)
            journalEntryHelper.updateJournalEntryForOBS(loanProductEntity,journalEntry,obsAmount);
        updateParamsForJournalEntryBeforePersist(journalEntry, transactionId, loanAccount, rollupDueDate,
                PENALTY_ROLLUP_MESSAGE, PENALTY_ROLLUP_MESSAGE);
        return journalEntry;
    }

    private void updateParamsForJournalEntryBeforePersist(JournalEntry journalEntry, Long transactionId, LoanAccount loanAccount,
                                                          String transactionDate, String note, String message) {
        journalEntry.setTransactionIdentifier(transactionId.toString());
        journalEntry.setTransactionDate(DateConverter.toIsoString(LocalDateTime.now(Clock.systemUTC())));
        journalEntry.setTransactionType(String.valueOf(EventType.PNLTYWOF));
        journalEntry.setClerk(UserContextHolder.checkedGetUser());
        journalEntry.setNote(note);
        journalEntry.setMessage(message);
        journalEntry.setEntityId(loanAccount.getLoanAccountNumber());
        journalEntry.setEntityName("portfolio");
        journalEntry.setEntityEvent(String.valueOf(EventType.PNLTYWOF));
        journalEntry.setCurrency(loanAccount.getCurrencyCode());
        journalEntry.setManualEntry(false);
        journalEntry.setState(JournalEntry.State.PENDING.name());
        journalEntry.setActualTransactionDate(transactionDate);
        journalEntry.setCreatedOn(DateConverter.toIsoString(LocalDateTime.now(Clock.systemUTC())));
        journalEntry.setCreatedBy(UserContextHolder.checkedGetUser());
    }

    private void createJournalEntry(LoanTransactionEntity persistedTransactionEntry,
                                    JournalEntry journalEntry, String comment, Command command) {
        String user = "System";
        try {
            user = UserContextHolder.checkedGetUser();
        } catch (Exception ignored) {
        }
        if (isListenerEnabled) {
            JournalPayload payLoad = new JournalPayload();
            payLoad.setTransactionId(persistedTransactionEntry.getId());
            journalEntryValidator.checkBeforeJournalEntry(journalEntry);
            command.handleEventTransaction(eventRepository, user, applicationName,
                    this.gson.toJson(payLoad), persistedTransactionEntry.getId().toString());
            persistedTransactionEntry.setJournalEntryBody(JsonConverter.convertObjectToJSON(journalEntry));
            transactionService.updateTransaction(persistedTransactionEntry);
        } else {
            journalEntryValidator.checkBeforeJournalEntry(journalEntry);
            applicationAccess.generateInternalToken(AppConstants.ACCOUNTING, AppConstants.PORTFOLIO);
            this.ledgerManager.createJournalEntry(journalEntry, comment);
        }
    }

    private boolean checkIfPenaltyDueForRollup(LoanAccount  loanAccount, LoanScheduleModelRepaymentPeriodEntity installment) {
        return LoanModel.IS_IR_MODEL.test(findLoanModelForInstallment(loanAccount,installment.getModelNumber()))
                && (NumberUtils.isNotZero(installment.getCapitalizedInterest())
                || (NumberUtils.isZero(installment.getInterestDue())
                && NumberUtils.isNotZero(installment.getOverDue())));
    }

    private LoanModel findLoanModelForInstallment(LoanAccount loanAccount, int periodNumber) {
        return loanAccount
                .getLoanModels()
                .stream()
                .filter(x -> x.getPeriodNumber() == periodNumber)
                .findFirst()
                .orElseThrow(() -> ServiceException.notFound("Loan Model Not Found !"));
    }

    private void createDemandsForPenaltyApplication(LoanAccount loanAccount, List<RepaymentAmount> penaltyDues,
                                                    List<DeferredDemandEntity> penaltyDeferredDemands, Long eventId,
                                                    boolean rollupFlag, LocalDate runDate) {
        if (rollupFlag) {
            List<RepaymentAmount> filteredDues = penaltyDues.stream()
                    .filter(due -> NumberUtils.isGreaterThanZero(due.getDueAmount()))
                    .collect(Collectors.toList());
            BigDecimal totalCollectedAmount = filteredDues.stream().map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (NumberUtils.isGreaterThanZero(totalCollectedAmount)) {
                CollectionEntity collection = CollectionEntity.createCollectionEntity(loanAccount.getIdentifier(),
                        eventId, runDate.toString(), totalCollectedAmount, loanAccount.getCurrencyCode(), BigDecimal.ZERO,
                        BigDecimal.ZERO, false, EventType.PENROLP);
                CollectionEntity persistedCollection = collectionRepository.save(collection);
                List<DemandCollectionEntity> dcsToPersist = Lists.newArrayList();
                for (RepaymentAmount due : filteredDues) {
                    if (Objects.isNull(due.getDemandId())) {
                        DemandEntity demandEntity = DemandEntity.createDemandEntity(loanAccount.getIdentifier(), due.getTrancheId(),
                                EventType.PENROLP, eventId, null, null, due.getInstallmentNo(),
                                LocalDate.parse(due.getDueDate()), BigDecimal.ZERO, due.getDueAmount(), loanAccount.getCurrencyCode(),
                                Constants.PENALTY_ROLLUP_REMARKS);
                        DemandEntity persistedDemand = demandRepository.save(demandEntity);
                        DemandCollectionEntity dc = DemandCollectionEntity.createDemandCollectionEntity(loanAccount.getIdentifier(),
                                persistedCollection, persistedDemand, BigDecimal.ZERO, BigDecimal.ZERO, loanAccount.getCurrencyCode(), due.getDueAmount());
                        dcsToPersist.add(dc);
                    } else {
                        DemandEntity demandEntity = demandRepository.getById(due.getDemandId());
                        DemandCollectionEntity dc = DemandCollectionEntity.createDemandCollectionEntity(loanAccount.getIdentifier(),
                                persistedCollection, demandEntity, BigDecimal.ZERO, BigDecimal.ZERO, loanAccount.getCurrencyCode(), due.getDueAmount());
                        dcsToPersist.add(dc);
                    }
                }
                demandCollectionRepository.saveAll(dcsToPersist);
            }
        } else {
            for (RepaymentAmount due : penaltyDues) {
                if (NumberUtils.isLessThanEqualsZero(due.getDueAmount())) continue;
                DemandEntity demandEntity = DemandEntity.createDemandEntity(loanAccount.getIdentifier(), due.getTrancheId(),
                        EventType.PNLTYAPP, eventId, null, null, due.getInstallmentNo(),
                        LocalDate.parse(due.getDueDate()), BigDecimal.ZERO, due.getDueAmount(), loanAccount.getCurrencyCode(),
                        Constants.PENALTY_APP_REMARKS);
                demandRepository.save(demandEntity);
            }
        }
        penaltyDeferredDemands.forEach(x->x.setCurrentDeferredAmount(BigDecimal.ZERO));
        deferDemandService.persist(penaltyDeferredDemands);
    }

    private LoanMasterDTO getLoanMasterDTOForPenalty(LoanAccount loanAccount, List<LoanPenaltyInterestHistoryEntity> penaltyInterestHistoryEntities) {
        LoanMasterDTO loanMasterDTO;
        if (penaltyInterestHistoryEntities.stream().anyMatch(x -> Objects.nonNull(x.getPenalCalculationInterestMethod())
                && !PenalCalculationInterestMethod.NONE.equals(x.getPenalCalculationInterestMethod()))) {
            loanMasterDTO = loanMasterDTOUtil.createCustomDTO(loanAccount, true, true,
                    true, true, true, true,
                    true, true, true, true, true,
                    false, LoanAccountUtil.IS_MIGRATED_LOAN.test(loanAccount));
        } else {
            loanMasterDTO = loanMasterDTOUtil.createCustomDTO(loanAccount, true, true,
                    true, true, true, true,
                    true, false, true, true, false,
                    false, LoanAccountUtil.IS_MIGRATED_LOAN.test(loanAccount));
        }
        return loanMasterDTO;
    }

    public List<RepaymentAmount> addToPenaltyTransferredDues(LoanAccount loanAccount,
                                                             DisbursementData disbursementData,
                                                             LocalDate endDate,RepositoryDTO repositoryDTO,LoanMasterDTO loanMasterDTO) {
        BigDecimal penaltyCalculated = BigDecimal.ZERO;
        List<RepaymentAmount> penaltyDues = new ArrayList<>();
        BigDecimal existingDueBetween = BigDecimal.ZERO;
        List<Demand> allDemands = Objects.nonNull(repositoryDTO) ? repositoryDTO.getDemands() : loanMasterDTO.getDemandList();
        List<DemandCollection> allDemandCollection = Objects.nonNull(repositoryDTO) ? repositoryDTO.getDemandCollections() : loanMasterDTO.getDemandCollectionList();
        List<Demand> demands = allDemands.stream().filter(x ->
                        !x.getReversed() && x.getDemandType() == DemandType.PENALTY
                                && EventType.TRNSFRIN.equals(x.getEventType())
                                && disbursementData.getIdentifier().equals(x.getTrancheId())
                                && !DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isBefore(
                                LocalDate.parse(disbursementData.getActualDisbursementDate()))
                                && !DateConverter.dateFromIsoString(x.getDemandDate() + "Z").isAfter(endDate))
                .collect(Collectors.toList());

        for (Demand d : demands) {
            existingDueBetween = existingDueBetween.add(d.getDueAmount());
            BigDecimal collectedAmount = allDemandCollection.stream()
                    .filter(x -> !x.getReversed() && x.getDemandId().equals(d.getId()))
                    .map(x -> x.getApportionedAndTransferredAmount().add(x.getWaivedAmount()).add(x.getCapitalizedAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal dueAmount = d.getDueAmount().subtract(collectedAmount);
            boolean isDueAdded = penaltyDues.stream().anyMatch(x-> Objects.nonNull(x.getDemandId()) && x.getDemandId().equals(d.getId()))
                    || (Objects.nonNull(repositoryDTO) &&
                    repositoryDTO.getDemands().stream().anyMatch(x-> Objects.nonNull(x.getId()) && x.getId().equals(d.getId())));
            if (NumberUtils.isGreaterThanZero(dueAmount) && !isDueAdded) {
                RepaymentAmount penaltyGenerated = generatePenaltyAmount(d.getId(), d.getDemandDate(),
                        d.getInstallmentNo(), dueAmount, disbursementData.getIdentifier(), loanMasterDTO.getLoanProduct());
                penaltyDues.add(penaltyGenerated);
            }
        }
        BigDecimal currentPenaltyDue = penaltyDues.stream().filter(x ->
                        !DateConverter.dateFromIsoString(x.getDueDate() + "Z").isBefore(
                                LocalDate.parse(disbursementData.getActualDisbursementDate()))
                                && !DateConverter.dateFromIsoString(x.getDueDate() + "Z").isAfter(endDate))
                .map(RepaymentAmount::getDueAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        penaltyCalculated = penaltyCalculated.subtract(existingDueBetween).subtract(currentPenaltyDue);
        if (NumberUtils.isGreaterThanZero(penaltyCalculated)) {
            long installmentNo = getInstallmentNoForPenaltyDemand(loanAccount,disbursementData,endDate);
            RepaymentAmount penaltyGenerated = generatePenaltyAmount(null, endDate.toString(),
                    installmentNo, penaltyCalculated, disbursementData.getIdentifier(), loanMasterDTO.getLoanProduct());
            penaltyDues.add(penaltyGenerated);
        }
        return penaltyDues;
    }

    public boolean checkIfPenaltyRollupPendingForDate(LoanAccount loanAccount,LocalDate queryDate){
        Optional<LoanScheduleModelRepaymentPeriod> lastIpdBeforeQueryDate = loanAccount.getRepaymentPeriodInstallments()
                .stream()
                .filter(ipd -> LocalDate.parse(ipd.getInstallmentDate()).isBefore(queryDate)
                        && NumberUtils.isZero(ipd.getInterestDue())
                        && LoanModel.IS_IR_MODEL.test(findLoanModelForInstallment(loanAccount,ipd.getModelNumber())))
                .sorted(Comparator.comparing(LoanScheduleModelRepaymentPeriod::getInstallmentDate))
                .reduce((first,second) -> second);
        if(lastIpdBeforeQueryDate.isPresent()){
            LocalDate penaltyDueDate = LocalDate.parse(loanAccount.getPenaltyApplicationDueDate());
            return !penaltyDueDate.isAfter(LocalDate.parse(lastIpdBeforeQueryDate.get().getInstallmentDate()));
        }
        return false;

    }

    public Optional<LoanPenaltyInterestHistoryEntity> fetchLatestPenaltyStartEventBeforeDate(LocalDate eventDate, Long loanId) {
        Optional<LoanPenaltyInterestHistoryEntity> latestPenaltyHistory =
                loanPenaltyIntHistoryRepository.findTopByLoanIdAndEndDateIsNullAndIsDeletedFalseOrderByIdDesc(loanId);
        return latestPenaltyHistory.isPresent() && latestPenaltyHistory.get().getStartDate().isBefore(eventDate) ? latestPenaltyHistory : Optional.empty();
    }

    public boolean isPenaltyApplicationDue(Long loanId){
        return !isPenaltyStopped(loanId) || isPenaltyDeferredDemandPending(loanId);
    }

    private boolean isPenaltyStopped(Long loanId) {
        Optional<LoanPenaltyInterestHistoryEntity> lastPenaltyHistory = loanPenaltyIntHistoryRepository
                .findTopByLoanIdAndEndDateIsNullAndIsDeletedFalseOrderByIdDesc(loanId);
        return !lastPenaltyHistory.isPresent();
    }

    private boolean isPenaltyDeferredDemandPending(Long loanId){
        List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                .fetchAllNonZeroDeferredDemandEntitiesForPenalty(loanId);
        return !deferredDemandEntities.isEmpty();
    }

    private void createDemandsForRestructuredDemands(LoanAccount loanAccount, Long eventId, LocalDate eventDate,
                                                     LoanMasterDTO loanMasterDTO, String remarks) {
        List<DeferredDemandEntity> deferredDemandEntities = deferDemandService
                .fetchAllNonZeroDeferredDemandEntitiesForPenalty(loanAccount.getIdentifier());
        if (!deferredDemandEntities.isEmpty()) {
            for (DeferredDemandEntity dd : deferredDemandEntities) {
                Optional<Demand> demandForDeferredDemand = loanMasterDTO.getDemandList().stream()
                        .filter(d -> d.getId().equals(dd.getDemandId())).findFirst();
                if (demandForDeferredDemand.isPresent()) {
                    DemandEntity demand = DemandEntity.createDemandEntity(loanAccount.getIdentifier(),
                            demandForDeferredDemand.get().getTrancheId(), EventType.PNLTYINT,
                            eventId, null, null, demandForDeferredDemand.get().getInstallmentNo(), eventDate,
                            BigDecimal.ZERO, dd.getCurrentDeferredAmount(), loanAccount.getCurrencyCode(), remarks);
                    dd.setDemandId(demandRepository.save(demand).getId());
                    dd.setCurrentDeferredAmount(BigDecimal.ZERO);
                    dd.setLastModifiedBy(UserContextHolder.checkedGetUser());
                    dd.setLastModifiedOn(LocalDateTime.now(Clock.systemUTC()));
                }
            }
            deferDemandService.persist(deferredDemandEntities);
        }
    }

    public List<RepaymentAmount> getPendingPenaltyDueForRollUp(LoanAccountEntity loanAccountEntity,
                                                               LocalDate duedate,
                                                               List<RepaymentAmount> penaltyDues){

        Optional<LoanScheduleModelRepaymentPeriodEntity> lastIPDOptional = loanAccountEntity.getRepaymentPeriodInstallments().stream()
                .filter(ipd ->  ipd.getInterestPayingIpd() && ipd.getInstallmentDate().isBefore(duedate))
                .max(LoanScheduleModelRepaymentPeriodEntity.InstallmentComparator);
        //Checking if there is an ipd just before duedate and interest due is more than 0 on this ipd
        List<RepaymentAmount> dueForRollUps = new ArrayList<>();
        if(lastIPDOptional.isPresent()){
            LoanScheduleModelRepaymentPeriodEntity lastIPD = lastIPDOptional.get();
            //checking if the last ipd belongs to rollup model
            boolean isRollupIPD = loanAccountEntity.getLoanModels().stream()
                    .filter(x-> lastIPD.getModelNumber().equals(x.getPeriodNumber())).findFirst()
                    .map(LoanModelEntity.IS_IR_MODEL::test)
                    .orElseThrow(()-> ServiceException.notFound("LoanModel not found"));
            if(isRollupIPD){
                //fetching installments of all tranche for the last ipd installment date
                List<LoanScheduleModelRepaymentPeriodEntity> installments = loanAccountEntity.getRepaymentPeriodInstallments().stream()
                        .filter(ipd -> ipd.getInstallmentDate().equals(lastIPD.getInstallmentDate()))
                        .collect(Collectors.toList());

                for(LoanScheduleModelRepaymentPeriodEntity ipd : installments){
                    dueForRollUps.addAll(penaltyDues.stream().filter(due -> Objects.nonNull(due.getDemandId())
                            && due.getTrancheId().equals(ipd.getTrancheId())
                            && due.getInstallmentNo().intValue() == ipd.getPeriodNumber()).collect(Collectors.toList()));
                }
            }
        }
        return  dueForRollUps;

    }
}

