package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class RemoteDeadlockMonitor {

    private final ThreadMXBean threadBean;

    public RemoteDeadlockMonitor(
            MBeanServerConnection connection) throws IOException {

        this.threadBean =
                ManagementFactory.getPlatformMXBean(
                        connection,
                        ThreadMXBean.class
                );
    }

    public void detectDeadlocks() {

        System.out.println();
        System.out.println("===== DEADLOCK DETECTION =====");

        long[] deadlockedThreads =
                threadBean.findDeadlockedThreads();

        if (deadlockedThreads == null
                || deadlockedThreads.length == 0) {

            System.out.println(
                    "No deadlock detected."
            );

            return;
        }

        System.out.println(
                "⚠ DEADLOCK DETECTED"
        );

        System.out.println(
                "Deadlocked Threads: "
                        + deadlockedThreads.length
        );

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(
                        deadlockedThreads
                );

        for (ThreadInfo threadInfo : threadInfos) {

            if (threadInfo == null) {
                continue;
            }

            System.out.println();
            System.out.println(
                    "-------------------------------"
            );

            System.out.println(
                    "Thread ID: "
                            + threadInfo.getThreadId()
            );

            System.out.println(
                    "Thread Name: "
                            + threadInfo.getThreadName()
            );

            System.out.println(
                    "State: "
                            + threadInfo.getThreadState()
            );

            System.out.println(
                    "Waiting For: "
                            + threadInfo.getLockName()
            );

            System.out.println(
                    "Lock Owner: "
                            + threadInfo.getLockOwnerName()
            );

            System.out.println(
                    "Lock Owner ID: "
                            + threadInfo.getLockOwnerId()
            );
        }
    }
}