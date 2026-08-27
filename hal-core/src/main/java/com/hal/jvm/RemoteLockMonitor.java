package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class RemoteLockMonitor {

    private final ThreadMXBean threadBean;

    public RemoteLockMonitor(
            MBeanServerConnection connection) throws IOException {

        this.threadBean =
                ManagementFactory.getPlatformMXBean(
                        connection,
                        ThreadMXBean.class
                );
    }

    public void printLockInformation() {

        System.out.println();
        System.out.println("===== THREAD LOCKS =====");

        long[] threadIds =
                threadBean.getAllThreadIds();

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(threadIds);

        for (ThreadInfo threadInfo : threadInfos) {

            if (threadInfo == null) {
                continue;
            }

            System.out.println();
            System.out.println("-------------------------------");

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
                    "Lock Name: "
                            + threadInfo.getLockName()
            );

            System.out.println(
                    "Lock Owner ID: "
                            + threadInfo.getLockOwnerId()
            );

            System.out.println(
                    "Lock Owner Name: "
                            + threadInfo.getLockOwnerName()
            );
        }
    }
}