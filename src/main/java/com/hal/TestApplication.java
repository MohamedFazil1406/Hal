package com.hal;

public class TestApplication {

    private static final Object LOCK_A =
            new Object();

    private static final Object LOCK_B =
            new Object();

    public static void main(String[] args)
            throws Exception {

        System.out.println(
                "Test application is running..."
        );

        System.out.println(
                "PID: " +
                        ProcessHandle.current().pid()
        );

        Thread thread1 = new Thread(() -> {

            synchronized (LOCK_A) {

                System.out.println(
                        "Thread-1 acquired LOCK_A"
                );

                sleep();

                synchronized (LOCK_B) {

                    System.out.println(
                            "Thread-1 acquired LOCK_B"
                    );
                }
            }

        }, "Deadlock-Thread-1");


        Thread thread2 = new Thread(() -> {

            synchronized (LOCK_B) {

                System.out.println(
                        "Thread-2 acquired LOCK_B"
                );

                sleep();

                synchronized (LOCK_A) {

                    System.out.println(
                            "Thread-2 acquired LOCK_A"
                    );
                }
            }

        }, "Deadlock-Thread-2");


        thread1.start();
        thread2.start();

        // Keep JVM alive
        while (true) {
            Thread.sleep(1000);
        }
    }

    private static void sleep() {

        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}