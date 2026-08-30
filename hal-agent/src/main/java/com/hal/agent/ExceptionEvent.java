package com.hal.agent;

public record ExceptionEvent(
        String exceptionClass,
        String message,
        String className,
        String methodName,
        String location
) {
}