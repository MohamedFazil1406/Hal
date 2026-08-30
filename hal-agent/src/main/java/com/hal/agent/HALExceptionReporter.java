package com.hal.agent;

import java.io.PrintWriter;
import java.net.Socket;

public class HALExceptionReporter {

    private static final String HOST = "localhost";
    private static final int PORT = 5005;

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

        send(event);
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

    private static void send(
            ExceptionEvent event) {

        try (
                Socket socket =
                        new Socket(HOST, PORT);

                PrintWriter writer =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        )
        ) {

            writer.println(
                    event.exceptionClass()
                            + "|"
                            + event.message()
                            + "|"
                            + event.className()
                            + "|"
                            + event.methodName()
                            + "|"
                            + event.location()
            );

            System.out.println(
                    "Exception event sent to HAL."
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not send exception event to HAL."
            );
        }
    }
}