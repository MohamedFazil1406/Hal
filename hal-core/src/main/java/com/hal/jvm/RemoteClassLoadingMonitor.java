package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

public class RemoteClassLoadingMonitor {

    private final ClassLoadingMXBean classLoadingBean;

    public RemoteClassLoadingMonitor(
            MBeanServerConnection connection) throws IOException {

        this.classLoadingBean =
                ManagementFactory.getPlatformMXBean(
                        connection,
                        ClassLoadingMXBean.class
                );
    }

    public void printClassLoadingInformation() {

        System.out.println();
        System.out.println("===== CLASS LOADING =====");

        System.out.println(
                "Loaded Classes: "
                        + classLoadingBean.getLoadedClassCount()
        );

        System.out.println(
                "Total Loaded Classes: "
                        + classLoadingBean.getTotalLoadedClassCount()
        );

        System.out.println(
                "Unloaded Classes: "
                        + classLoadingBean.getUnloadedClassCount()
        );
    }
}