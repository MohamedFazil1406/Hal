package com.hal.test;

public class DeadlockTest {

    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void start() {

        Thread threadA = new Thread(() -> {

            synchronized (LOCK_A) {

                System.out.println(
                        "Thread-A acquired LOCK_A"
                );

                sleep();

                synchronized (LOCK_B) {

                    System.out.println(
                            "Thread-A acquired LOCK_B"
                    );
                }
            }

        }, "Deadlock-Thread-A");

        Thread threadB = new Thread(() -> {

            synchronized (LOCK_B) {

                System.out.println(
                        "Thread-B acquired LOCK_B"
                );

                sleep();

                synchronized (LOCK_A) {

                    System.out.println(
                            "Thread-B acquired LOCK_A"
                    );
                }
            }

        }, "Deadlock-Thread-B");

        threadA.start();
        threadB.start();
    }

    private static void sleep() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}