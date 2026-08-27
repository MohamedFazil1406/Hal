package com.hal;

import com.hal.incident.Incident;
import com.hal.incident.IncidentManager;
import com.hal.incident.IncidentSeverity;
import com.hal.incident.IncidentType;
import com.hal.jvm.*;
import com.hal.process.JVMDiscovery;

import com.sun.tools.attach.VirtualMachine;

import javax.management.MBeanServerConnection;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("        HAL JVM DISCOVERY");
        System.out.println("================================");

        JVMDiscovery discovery =
                new JVMDiscovery();

        List<JVMInfo> jvms =
                discovery.discover();

        if (jvms.isEmpty()) {

            System.out.println(
                    "No Java applications found."
            );

            return;
        }

        // Display JVMs
        for (int i = 0; i < jvms.size(); i++) {

            JVMInfo jvm = jvms.get(i);

            System.out.println();
            System.out.println(
                    "[" + (i + 1) + "]"
            );

            System.out.println(
                    "PID: " + jvm.getPid()
            );

            System.out.println(
                    "Command: " + jvm.getCommand()
            );

            System.out.println(
                    "-------------------------------"
            );
        }

        // Select JVM
        Scanner scanner =
                new Scanner(System.in);

        System.out.print(
                "\nSelect JVM: "
        );

        int choice =
                scanner.nextInt();

        if (choice < 1 ||
                choice > jvms.size()) {

            System.out.println(
                    "Invalid selection."
            );

            return;
        }

        JVMInfo selectedJVM =
                jvms.get(choice - 1);

        System.out.println();

        System.out.println(
                "Selected PID: " +
                        selectedJVM.getPid()
        );

        // Attach
        JVMConnector connector =
                new JVMConnector();

        VirtualMachine vm =
                connector.attach(
                        selectedJVM.getPid()
                );

        if (vm == null) {
            return;
        }

        JMXConnectorService jmx =
                new JMXConnectorService();

        try {

            // Connect to JMX
            MBeanServerConnection connection =
                    jmx.connect(vm);

            // Monitor memory
            RemoteJVMMonitor monitor =
                    new RemoteJVMMonitor();

            monitor.showMemory(connection);

            RemoteCpuMonitor cpuMonitor =
                    new RemoteCpuMonitor(connection);

            cpuMonitor.printTopCpuThreads();

            RemoteGcMonitor gcMonitor =
                    new RemoteGcMonitor(connection);

            gcMonitor.printGcInformation();

            RemoteClassLoadingMonitor classMonitor =
                    new RemoteClassLoadingMonitor(connection);

            classMonitor.printClassLoadingInformation();

            RemoteRuntimeMonitor runtimeMonitor =
                    new RemoteRuntimeMonitor(connection);

            runtimeMonitor.printRuntimeInformation();

            RemoteLockMonitor lockMonitor =
                    new RemoteLockMonitor(connection);

            lockMonitor.printLockInformation();

            RemoteDeadlockMonitor deadlockMonitor =
                    new RemoteDeadlockMonitor(connection);

            deadlockMonitor.detectDeadlocks();

            RemoteStackTraceMonitor stackMonitor =
                    new RemoteStackTraceMonitor(connection);

            stackMonitor.printStackTraces();

            RemoteThreadMonitor threadMonitor =
                    new RemoteThreadMonitor();

            threadMonitor.showThreads(connection);

            IncidentManager incidentManager =
                    new IncidentManager();

            Incident incident1 =
                    new Incident(
                            IncidentType.DEADLOCK,
                            IncidentSeverity.CRITICAL,
                            "JVM deadlock detected",
                            "Two threads are waiting for each other's locks.",
                            "Deadlock-Thread-A",
                            "DeadlockTest.java"
                    );

            Incident incident2 =
                    new Incident(
                            IncidentType.HIGH_CPU,
                            IncidentSeverity.WARNING,
                            "High CPU usage detected",
                            "A thread is consuming significant CPU.",
                            "CPU-Heavy-Thread",
                            "TestApplication.java"
                    );

            incidentManager.addIncident(incident1);
            incidentManager.addIncident(incident2);

            incidentManager.printIncidents();

            DeadlockDetector deadlockDetector =
                    new DeadlockDetector();

            deadlockDetector.detect(connection);


            // Disconnect JMX
            jmx.disconnect();

        } catch (Exception e) {

            System.out.println(
                    "JMX monitoring failed."
            );

            e.printStackTrace();

        } finally {

            // Detach from JVM
            connector.detach();
        }
    }
}