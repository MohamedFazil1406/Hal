package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RemoteCpuMonitor {

    private final ThreadMXBean threadBean;

    public RemoteCpuMonitor(
            MBeanServerConnection connection)
            throws IOException {

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

    public void printTopCpuThreads()
            throws Exception {

        System.out.println();
        System.out.println(
                "===== TOP CPU THREADS ====="
        );

        List<ThreadCpuData> cpuData =
                getTopCpuThreads();

        for (int i = 0;
             i < cpuData.size();
             i++) {

            ThreadCpuData data =
                    cpuData.get(i);

            ThreadInfo threadInfo =
                    getThreadInfo(
                            data.threadId()
                    );

            if (threadInfo == null) {
                continue;
            }

            System.out.println();
            System.out.println(
                    "[" + (i + 1) + "]"
            );

            System.out.println(
                    "Thread ID: "
                            + data.threadId()
            );

            System.out.println(
                    "Thread Name: "
                            + threadInfo.getThreadName()
            );

            System.out.println(
                    "State: "
                            + threadInfo.getThreadState()
            );

            System.out.printf(
                    "CPU Usage: %.2f%%%n",
                    data.getCpuPercentage()
            );

            System.out.println("Location:");

            StackTraceElement[] stack =
                    threadInfo.getStackTrace();

            if (stack.length > 0) {

                System.out.println(
                        "    at " + stack[0]
                );

            } else {

                System.out.println(
                        "    No stack trace available"
                );
            }
        }
    }

    public List<ThreadCpuData> getTopCpuThreads()
            throws Exception {

        long[] threadIds =
                threadBean.getAllThreadIds();

        List<ThreadCpuData> cpuData =
                new ArrayList<>();

        // First measurement
        for (long threadId : threadIds) {

            long cpuTime =
                    threadBean.getThreadCpuTime(
                            threadId
                    );

            if (cpuTime != -1) {

                cpuData.add(
                        new ThreadCpuData(
                                threadId,
                                cpuTime
                        )
                );
            }
        }

        // Measurement interval
        Thread.sleep(1000);

        // Second measurement
        for (ThreadCpuData data : cpuData) {

            long after =
                    threadBean.getThreadCpuTime(
                            data.threadId()
                    );

            if (after == -1) {

                data.setCpuDelta(0);

                continue;
            }

            data.setCpuDelta(
                    after - data.beforeCpuTime()
            );
        }

        // Highest CPU first
        cpuData.sort(
                Comparator.comparingLong(
                        ThreadCpuData::cpuDelta
                ).reversed()
        );

        return cpuData.subList(
                0,
                Math.min(5, cpuData.size())
        );
    }

    public ThreadInfo getThreadInfo(
            long threadId) {

        return threadBean.getThreadInfo(
                threadId
        );
    }

    public static class ThreadCpuData {

        private final long threadId;

        private final long beforeCpuTime;

        private long cpuDelta;

        public ThreadCpuData(
                long threadId,
                long beforeCpuTime) {

            this.threadId = threadId;
            this.beforeCpuTime = beforeCpuTime;
        }

        public long threadId() {
            return threadId;
        }

        public long beforeCpuTime() {
            return beforeCpuTime;
        }

        public long cpuDelta() {
            return cpuDelta;
        }

        public void setCpuDelta(
                long cpuDelta) {

            this.cpuDelta = cpuDelta;
        }

        public double getCpuPercentage() {

            return (
                    cpuDelta
                            / 1_000_000_000.0
            ) * 100.0;
        }
    }
}