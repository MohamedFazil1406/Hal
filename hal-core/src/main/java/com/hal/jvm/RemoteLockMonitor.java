package com.hal.jvm;

import com.hal.detector.LockContentionDetector;
import com.hal.incident.IncidentManager;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class RemoteLockMonitor {

    private final ThreadMXBean threadBean;

    public RemoteLockMonitor(
            MBeanServerConnection connection)
            throws IOException {

        this.threadBean =
                ManagementFactory.getPlatformMXBean(
                        connection,
                        ThreadMXBean.class
                );
    }

    public void printLockInformation() {

        System.out.println();
        System.out.println(
                "===== THREAD LOCKS ====="
        );

        long[] threadIds =
                threadBean.getAllThreadIds();

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(threadIds);

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

    public void detectLockContention(
            IncidentManager incidentManager) {

        System.out.println();
        System.out.println(
                "===== LOCK CONTENTION DETECTION ====="
        );

        long[] threadIds =
                threadBean.getAllThreadIds();

        ThreadInfo[] threadInfos =
                threadBean.getThreadInfo(threadIds);

        LockContentionDetector detector =
                new LockContentionDetector();

        int detected = 0;

        for (ThreadInfo threadInfo : threadInfos) {

            if (threadInfo == null) {
                continue;
            }

            if (threadInfo.getThreadState()
                    != Thread.State.BLOCKED) {

                continue;
            }

            String lockName =
                    threadInfo.getLockName();

            String lockOwnerName =
                    threadInfo.getLockOwnerName();

            if (lockName == null) {
                continue;
            }

            System.out.println();
            System.out.println(
                    "⚠ LOCK CONTENTION DETECTED"
            );

            System.out.println(
                    "Thread: "
                            + threadInfo.getThreadName()
            );

            System.out.println(
                    "Waiting For: "
                            + lockName
            );

            System.out.println(
                    "Lock Owner: "
                            + lockOwnerName
            );

            detector.check(
                    threadInfo.getThreadId(),
                    threadInfo.getThreadName(),
                    lockName,
                    lockOwnerName,
                    incidentManager
            );

            detected++;
        }

        if (detected == 0) {

            System.out.println(
                    "No blocked threads detected."
            );
        }
    }
}