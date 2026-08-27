package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class RemoteThreadMonitor {

    public void showThreads(
            MBeanServerConnection connection)
            throws Exception {

        ThreadMXBean threadBean =
                ManagementFactory.newPlatformMXBeanProxy(
                        connection,
                        ManagementFactory.THREAD_MXBEAN_NAME,
                        ThreadMXBean.class
                );

        System.out.println();
        System.out.println("===== TARGET JVM THREADS =====");

        System.out.println(
                "Thread Count: " +
                        threadBean.getThreadCount()
        );

        System.out.println(
                "Peak Thread Count: " +
                        threadBean.getPeakThreadCount()
        );

        System.out.println(
                "Daemon Thread Count: " +
                        threadBean.getDaemonThreadCount()
        );

        System.out.println();

        long[] threadIds =
                threadBean.getAllThreadIds();

        for (long threadId : threadIds) {

            ThreadInfo threadInfo =
                    threadBean.getThreadInfo(threadId);

            if (threadInfo == null) {
                continue;
            }

            System.out.println(
                    "ID: " + threadInfo.getThreadId()
            );

            System.out.println(
                    "Name: " + threadInfo.getThreadName()
            );

            System.out.println(
                    "State: " + threadInfo.getThreadState()
            );

            System.out.println("-------------------------------");
        }
    }
}