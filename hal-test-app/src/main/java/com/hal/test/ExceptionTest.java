package com.hal.test;

public class ExceptionTest {

    public static void start() {

        Thread exceptionThread = new Thread(() -> {

            try {
                throw new RuntimeException(
                        "HAL test exception"
                );

            } catch (RuntimeException e) {

                System.out.println();
                System.out.println(
                        "===== TEST EXCEPTION ====="
                );

                e.printStackTrace();
            }

        }, "Exception-Test-Thread");

        exceptionThread.start();
    }
}