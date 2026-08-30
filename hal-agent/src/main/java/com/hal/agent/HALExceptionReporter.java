package com.hal.agent;

public class HALExceptionReporter {

    public static void report(
            Throwable throwable,
            String className,
            String methodName) {

        StackTraceElement[] stack =
                throwable.getStackTrace();

        String location = "Unknown";

        if (stack.length > 0) {
            location = stack[0].toString();
        }

        ExceptionEvent event =
                new ExceptionEvent(
                        throwable.getClass().getName(),
                        throwable.getMessage(),
                        className,
                        methodName,
                        location
                );

        print(event);
    }

    private static void print(
            ExceptionEvent event) {

        System.out.println();

        System.out.println(
                "===== HAL EXCEPTION DETECTED ====="
        );

        System.out.println(
                "Exception: "
                        + event.exceptionClass()
        );

        System.out.println(
                "Message: "
                        + event.message()
        );

        System.out.println(
                "Class: "
                        + event.className()
        );

        System.out.println(
                "Method: "
                        + event.methodName()
        );

        System.out.println(
                "Location: "
                        + event.location()
        );
    }
}