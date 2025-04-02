package concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NUmberGenerator {
    public static void main(String[] args) {

        //SequentialNumberPrinterCyclicBarrier.main(null);
        /*
        ExecutorService executor = Executors.newFixedThreadPool(4);
        NumbgerGenerator generator = new NumbgerGenerator(0,10);
        for (int i = 0; i < 10; i++) {
            executor.submit(()-> {
                try {
                    generator.generate();
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();*/
        ExecutorService executor = Executors.newFixedThreadPool(4);
        //NumbgerGeneratorV2 generator = new NumbgerGeneratorV2(4,100);
        NumbgerGenerator generator = new NumbgerGenerator(0,10);

            executor.submit(()-> {
                try {
                    while(generator.generate() <= 100) {
                    //generator.generate();
                        System.out.printf("");
                    }
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        executor.shutdown();
    }


    static class NumbgerGenerator {
        int start;
        int max ;
        AtomicInteger atomicInteger = new AtomicInteger();
        Object lock = new Object();
        public NumbgerGenerator(int start, int max) {
            this.start = start;
            this.max = max;
        }

        public Integer generate() throws BrokenBarrierException, InterruptedException {
            System.out.println("Thread name Number  "+Thread.currentThread().getId());
            synchronized (lock) {
               Integer current = atomicInteger.incrementAndGet();
                System.out.printf("Thread name %s Number -> %s \n",Thread.currentThread().getId(),current);
                //return current;
            }

            //System.out.println("done");
           return atomicInteger.get();
        }
    }
    static class NumbgerGeneratorV2 {
        int start;
        int max ;
        AtomicInteger atomicInteger = new AtomicInteger();
        CyclicBarrier cyclicBarrier;
        public NumbgerGeneratorV2(int start, int max) {
            this.start = start;
            this.max = max;
            cyclicBarrier = new CyclicBarrier(start,()-> {
                System.out.printf("Thread name %s Number -> %s \n",Thread.currentThread().getName(),atomicInteger.incrementAndGet());
            });
        }

        public Integer generate() throws BrokenBarrierException, InterruptedException {
            System.out.println("waiting");
            cyclicBarrier.await();
            System.out.println("done");
            atomicInteger.get();

            return atomicInteger.get();
        }
    }

     class SequentialNumberPrinterCyclicBarrier {
        private static final int MAX_NUMBER = 100;
        private  AtomicInteger currentNumber = new AtomicInteger(1);  // Atomic integer for thread-safe counting
        private static final int NUM_THREADS = 4;

        public  void main(String[] args) {
            ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

            // Create a CyclicBarrier that will trigger when all threads reach the barrier
            CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
                // After all threads arrive at the barrier, print the current number
                int number = currentNumber.getAndIncrement();
                if (number <= MAX_NUMBER) {
                    System.out.println(number);
                }
            });

            // Create a task for each thread
            Runnable task = () -> {
                try {
                    while (true) {
                        // Wait at the barrier
                        barrier.await();
                        if (currentNumber.get() > MAX_NUMBER) {
                            break;  // Exit if we've reached the maximum number
                        }
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            };

            // Submit NUM_THREADS tasks to the executor
            for (int i = 0; i < NUM_THREADS; i++) {
                executorService.submit(task);
            }

            // Shutdown the executor service
            executorService.shutdown();
        }
    }

}
