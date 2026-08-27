package com.hal.test;

public class TestApplication {

    public static void main(String[] args)
            throws Exception {

        System.out.println(
                "HAL Test Application is running..."
        );

        System.out.println(
                "PID: " +
                        ProcessHandle.current().pid()
        );

        Thread cpuThread = new Thread(() -> {

            while (true) {

                double result = 0;

                for (int i = 0; i < 10_000_000; i++) {
                    result += Math.sqrt(i);
                }

            }

        }, "CPU-Heavy-Thread");

        cpuThread.start();

        while (true) {
            Thread.sleep(1000);
        }
    }
}