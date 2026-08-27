package com.hal.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JVMMonitor {

    public void showMemory() {

        MemoryMXBean memoryBean =
                ManagementFactory.getMemoryMXBean();

        MemoryUsage heap =
                memoryBean.getHeapMemoryUsage();

        System.out.println();
        System.out.println("===== JVM MEMORY =====");

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

    private String formatMB(long bytes) {

        if (bytes < 0) {
            return "Unknown";
        }

        return (bytes / 1024 / 1024) + " MB";
    }
}