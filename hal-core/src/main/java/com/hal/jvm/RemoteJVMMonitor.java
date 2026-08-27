package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class RemoteJVMMonitor {

    public void showMemory(
            MBeanServerConnection connection)
            throws Exception {

        MemoryUsage heap =
                getHeapMemoryUsage(connection);

        System.out.println();
        System.out.println(
                "===== TARGET JVM MEMORY ====="
        );

        System.out.println(
                "Heap Used: " +
                        formatMB(heap.getUsed())
        );

        System.out.println(
                "Heap Committed: " +
                        formatMB(heap.getCommitted())
        );

        System.out.println(
                "Heap Max: " +
                        formatMB(heap.getMax())
        );
    }

    public MemoryUsage getHeapMemoryUsage(
            MBeanServerConnection connection)
            throws Exception {

        MemoryMXBean memoryBean =
                ManagementFactory.newPlatformMXBeanProxy(
                        connection,
                        ManagementFactory.MEMORY_MXBEAN_NAME,
                        MemoryMXBean.class
                );

        return memoryBean.getHeapMemoryUsage();
    }

    private String formatMB(long bytes) {

        if (bytes < 0) {
            return "Unknown";
        }

        return (bytes / 1024 / 1024) + " MB";
    }
}