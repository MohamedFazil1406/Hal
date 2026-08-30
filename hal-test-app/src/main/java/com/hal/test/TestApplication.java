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

        Thread.sleep(3000);

        System.out.println(
                "Throwing test exception..."
        );

        throw new RuntimeException(
                "HAL test exception"
        );
    }
}