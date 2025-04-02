package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PartitionEmployeesWithCollectorV1 {

    public static void main(String[] args) {
        List<Employee> employees =  List.of(
                new Employee("A",23,"1","M",12000.00),
                new Employee("B",33,"3","F",13000.00),
                new Employee("C",46,"2","F",22000.00),
                new Employee("D",22,"3","M",19000.00),
                new Employee("E",27,"1","M",22000.00),
                new Employee("F",29,"1","M",19000.00),
                new Employee("G",48,"3","M",17000.00)
        );

        List<List<Employee>> employePartionList = partitiion(employees,3);
        System.out.println(employePartionList);

    }

    private static List<List<Employee>> partitiion(List<Employee> employees, int i) {
        return employees.stream().collect(partitionCollector(i,Collectors.toList()));
    }
    static class PartionAccumulator {
        List<List<Employee>> employees = new ArrayList<>();
        int batchSize;

        public PartionAccumulator(int batchSize) {
            this.batchSize = batchSize;
        }

        public void add(Employee emp) {
            if(employees.size() == 0 || employees.get(employees.size()-1).size() == batchSize) employees.add(new ArrayList<>());
            employees.get(employees.size()-1).add(emp);
        }

        public List<List<Employee>> get() {
            return employees;
        }


        public PartionAccumulator combine(PartionAccumulator partionAccumulator2) {
            System.out.println("Threda_id"+Thread.currentThread().getName());
            partionAccumulator2.get().stream().forEach(list-> {
                if(list.size() == batchSize) {
                    employees.add(list);
                } else {
                    list.stream().forEach(this::add);
                }
            });
            return this;
        }
    }
    private static Collector<Employee,PartionAccumulator, List<List<Employee>>> partitionCollector(int partionSIze, Collector<Object, ?, List<Object>> toList) {

        Supplier<PartionAccumulator> supllier = ()->new PartionAccumulator(partionSIze);

        BiConsumer< PartionAccumulator,Employee> accumulator = PartionAccumulator::add;

        BinaryOperator<PartionAccumulator> combiner = PartionAccumulator::combine;

        Function<PartionAccumulator,List<List<Employee>>> finisher = (partionAccumulator1) -> partionAccumulator1.get();

        return Collector.of(supllier,accumulator,combiner,finisher,Collector.Characteristics.UNORDERED);
    }
}
