package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockDetector {

    public void detect(
            MBeanServerConnection connection)
            throws Exception {

        ThreadMXBean threadBean =
                ManagementFactory.newPlatformMXBeanProxy(
                        connection,
                        ManagementFactory.THREAD_MXBEAN_NAME,
                        ThreadMXBean.class
                );

        long[] deadlockedThreadIds =
                threadBean.findDeadlockedThreads();

        System.out.println();
        System.out.println("===== DEADLOCK DETECTION =====");

        if (deadlockedThreadIds == null) {

            System.out.println("No deadlock detected.");

            return;
        }

        System.out.println(
                "WARNING: DEADLOCK DETECTED!"
        );

        System.out.println(
                "Deadlocked Threads: " +
                        deadlockedThreadIds.length
        );

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(
                        deadlockedThreadIds,
                        true,
                        true
                );

        for (ThreadInfo threadInfo : threadInfos) {

            if (threadInfo == null) {
                continue;
            }

            System.out.println();
            System.out.println(
                    "Thread: " +
                            threadInfo.getThreadName()
            );

            System.out.println(
                    "Thread ID: " +
                            threadInfo.getThreadId()
            );

            System.out.println(
                    "State: " +
                            threadInfo.getThreadState()
            );

            System.out.println(
                    "Waiting On: " +
                            threadInfo.getLockName()
            );

            System.out.println(
                    "Lock Owner: " +
                            threadInfo.getLockOwnerName()
            );

            System.out.println(
                    "Lock Owner ID: " +
                            threadInfo.getLockOwnerId()
            );

            System.out.println("-------------------------------");
        }
    }
}