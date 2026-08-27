package com.hal.jvm;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;

public class RemoteRuntimeMonitor {

    private final RuntimeMXBean runtimeBean;

    public RemoteRuntimeMonitor(
            MBeanServerConnection connection) throws IOException {

        this.runtimeBean =
                ManagementFactory.getPlatformMXBean(
                        connection,
                        RuntimeMXBean.class
                );
    }

    public void printRuntimeInformation() {

        System.out.println();
        System.out.println("===== JVM RUNTIME =====");

        System.out.println(
                "Java Version: "
                        + runtimeBean.getSpecVersion()
        );

        System.out.println(
                "JVM Name: "
                        + runtimeBean.getVmName()
        );

        System.out.println(
                "JVM Vendor: "
                        + runtimeBean.getVmVendor()
        );

        System.out.println(
                "JVM Version: "
                        + runtimeBean.getVmVersion()
        );

        Map<String, String> properties =
                runtimeBean.getSystemProperties();

        System.out.println(
                "Java Home: "
                        + properties.get("java.home")
        );

        System.out.println(
                "Uptime: "
                        + runtimeBean.getUptime()
                        + " ms"
        );

        System.out.println(
                "Start Time: "
                        + runtimeBean.getStartTime()
        );
    }
}