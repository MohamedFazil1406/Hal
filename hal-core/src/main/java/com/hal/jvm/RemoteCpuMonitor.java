package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

public class RemoteCpuMonitor {

    private final ThreadMXBean threadBean;

    public RemoteCpuMonitor(MBeanServerConnection connection) throws IOException {

        this.threadBean =
                ManagementFactory.newPlatformMXBeanProxy(
                        connection,
                        ManagementFactory.THREAD_MXBEAN_NAME,
                        ThreadMXBean.class
                );

        if (threadBean.isThreadCpuTimeSupported()
                && !threadBean.isThreadCpuTimeEnabled()) {

            threadBean.setThreadCpuTimeEnabled(true);
        }
    }

    public void printTopCpuThreads() throws Exception {

        System.out.println();
        System.out.println("===== TARGET JVM CPU =====");

        long[] threadIds = threadBean.getAllThreadIds();

        Map<Long, Long> before = new HashMap<>();

        for (long threadId : threadIds) {

            long cpuTime =
                    threadBean.getThreadCpuTime(threadId);

            if (cpuTime != -1) {
                before.put(threadId, cpuTime);
            }
        }

        // Measure CPU usage over one second
        Thread.sleep(1000);

        long hottestThreadId = -1;
        long highestCpuTime = 0;

        for (long threadId : threadIds) {

            long after =
                    threadBean.getThreadCpuTime(threadId);

            Long beforeTime = before.get(threadId);

            if (beforeTime == null || after == -1) {
                continue;
            }

            long cpuDelta = after - beforeTime;

            if (cpuDelta > highestCpuTime) {
                highestCpuTime = cpuDelta;
                hottestThreadId = threadId;
            }
        }

        if (hottestThreadId == -1) {
            System.out.println("No CPU information available.");
            return;
        }

        ThreadInfo threadInfo =
                threadBean.getThreadInfo(hottestThreadId);

        if (threadInfo == null) {
            System.out.println("Thread no longer exists.");
            return;
        }

        double cpuPercentage =
                (highestCpuTime / 1_000_000_000.0) * 100.0;

        System.out.println();
        System.out.println("HOTTEST THREAD");
        System.out.println("-------------------------------");

        System.out.println(
                "Thread ID: " + hottestThreadId
        );

        System.out.println(
                "Thread Name: " + threadInfo.getThreadName()
        );

        System.out.println(
                "State: " + threadInfo.getThreadState()
        );

        System.out.printf(
                "CPU Usage: %.2f%%%n",
                cpuPercentage
        );

        System.out.println();
        System.out.println("Stack Trace:");

        for (StackTraceElement element :
                threadInfo.getStackTrace()) {

            System.out.println(
                    "    at " + element
            );
        }
    }
}