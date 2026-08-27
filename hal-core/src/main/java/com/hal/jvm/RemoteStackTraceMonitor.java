package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class RemoteStackTraceMonitor {

    private final ThreadMXBean threadBean;

    public RemoteStackTraceMonitor(MBeanServerConnection connection) throws IOException {

        this.threadBean =
                ManagementFactory.newPlatformMXBeanProxy(
                        connection,
                        ManagementFactory.THREAD_MXBEAN_NAME,
                        ThreadMXBean.class
                );
    }

    public void printStackTraces() {

        System.out.println();
        System.out.println("===== TARGET JVM STACK TRACES =====");

        long[] threadIds = threadBean.getAllThreadIds();

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(threadIds, Integer.MAX_VALUE);

        for (ThreadInfo threadInfo : threadInfos) {

            if (threadInfo == null) {
                continue;
            }

            System.out.println();
            System.out.println("--------------------------------");
            System.out.println(
                    "Thread ID: " + threadInfo.getThreadId()
            );

            System.out.println(
                    "Thread Name: " + threadInfo.getThreadName()
            );

            System.out.println(
                    "State: " + threadInfo.getThreadState()
            );

            System.out.println("Stack Trace:");

            for (StackTraceElement element :
                    threadInfo.getStackTrace()) {

                System.out.println(
                        "    at " + element
                );
            }
        }
    }
}