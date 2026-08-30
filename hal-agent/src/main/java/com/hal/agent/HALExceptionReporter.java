package com.hal.agent;

public class HALExceptionReporter {

    public static void report(
            Throwable throwable,
            String className,
            String methodName) {

        System.out.println();
        System.out.println(
                "===== HAL EXCEPTION DETECTED ====="
        );

        System.out.println(
                "Exception: "
                        + throwable.getClass().getName()
        );

        System.out.println(
                "Message: "
                        + throwable.getMessage()
        );

        System.out.println(
                "Class: "
                        + className
        );

        System.out.println(
                "Method: "
                        + methodName
        );

        StackTraceElement[] stack =
                throwable.getStackTrace();

        if (stack.length > 0) {

            System.out.println(
                    "Location: "
                            + stack[0]
            );
        }
    }
}