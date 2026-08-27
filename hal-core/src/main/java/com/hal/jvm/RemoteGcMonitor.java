package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class RemoteGcMonitor {

    private final List<GarbageCollectorMXBean> collectors;

    public RemoteGcMonitor(MBeanServerConnection connection) throws IOException {

        this.collectors =
                ManagementFactory.getPlatformMXBeans(
                        connection,
                        GarbageCollectorMXBean.class
                );
    }

    public void printGcInformation() {

        System.out.println();
        System.out.println("===== GARBAGE COLLECTION =====");

        long totalCollections = 0;
        long totalTime = 0;

        for (GarbageCollectorMXBean collector : collectors) {

            long collections =
                    collector.getCollectionCount();

            long time =
                    collector.getCollectionTime();

            if (collections != -1) {
                totalCollections += collections;
            }

            if (time != -1) {
                totalTime += time;
            }

            System.out.println();
            System.out.println(
                    "Collector: " + collector.getName()
            );

            System.out.println(
                    "Collections: " + collections
            );

            System.out.println(
                    "Time: " + time + " ms"
            );
        }

        System.out.println();
        System.out.println("-------------------------------");

        System.out.println(
                "Total Collections: "
                        + totalCollections
        );

        System.out.println(
                "Total GC Time: "
                        + totalTime
                        + " ms"
        );
    }
}