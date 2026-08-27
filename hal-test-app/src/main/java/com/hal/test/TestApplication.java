package com.hal.test;

public class TestApplication {

    public static void main(String[] args)
            throws Exception {

        System.out.println(
                "HAL Test Application is running..."
        );

        System.out.println(
                "PID: "
                        + ProcessHandle.current().pid()
        );

        DeadlockTest.start();

        while (true) {
            Thread.sleep(1000);
        }
    }
}