package com.hal;

import com.hal.detector.HighCpuDetector;
import com.hal.detector.MemoryDetector;
import com.hal.incident.IncidentManager;
import com.hal.jvm.*;
import com.hal.process.JVMDiscovery;

import com.sun.tools.attach.VirtualMachine;

import javax.management.MBeanServerConnection;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadInfo;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "================================"
        );

        System.out.println(
                "        HAL JVM DISCOVERY"
        );

        System.out.println(
                "================================"
        );

        // =========================
        // JVM DISCOVERY
        // =========================

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

        // =========================
        // DISPLAY JVMs
        // =========================

        for (int i = 0;
             i < jvms.size();
             i++) {

            JVMInfo jvm =
                    jvms.get(i);

            System.out.println();

            System.out.println(
                    "[" + (i + 1) + "]"
            );

            System.out.println(
                    "PID: "
                            + jvm.getPid()
            );

            System.out.println(
                    "Command: "
                            + jvm.getCommand()
            );

            System.out.println(
                    "-------------------------------"
            );
        }

        // =========================
        // SELECT JVM
        // =========================

        Scanner scanner =
                new Scanner(System.in);

        System.out.print(
                "\nSelect JVM: "
        );

        int choice =
                scanner.nextInt();

        if (choice < 1
                || choice > jvms.size()) {

            System.out.println(
                    "Invalid selection."
            );

            return;
        }

        JVMInfo selectedJVM =
                jvms.get(choice - 1);

        System.out.println();

        System.out.println(
                "Selected PID: "
                        + selectedJVM.getPid()
        );

        // =========================
        // ATTACH
        // =========================

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

            // =========================
            // JMX CONNECTION
            // =========================

            MBeanServerConnection connection =
                    jmx.connect(vm);

            // =========================
            // INCIDENT MANAGER
            // =========================

            IncidentManager incidentManager =
                    new IncidentManager();

            // =========================
            // MEMORY
            // =========================

            RemoteJVMMonitor monitor =
                    new RemoteJVMMonitor();

            monitor.showMemory(connection);

            MemoryUsage heapUsage =
                    monitor.getHeapMemoryUsage(connection);

            MemoryDetector memoryDetector =
                    new MemoryDetector(80.0);

            memoryDetector.check(
                    heapUsage.getUsed(),
                    heapUsage.getMax(),
                    incidentManager
            );

            // =========================
            // CPU
            // =========================

            RemoteCpuMonitor cpuMonitor =
                    new RemoteCpuMonitor(connection);

            HighCpuDetector highCpuDetector =
                    new HighCpuDetector(80.0);

            List<RemoteCpuMonitor.ThreadCpuData>
                    cpuThreads =
                    cpuMonitor.getTopCpuThreads();

            for (
                    RemoteCpuMonitor.ThreadCpuData thread
                    : cpuThreads
            ) {

                ThreadInfo threadInfo =
                        cpuMonitor.getThreadInfo(
                                thread.threadId()
                        );

                if (threadInfo == null) {
                    continue;
                }

                highCpuDetector.check(
                        thread.threadId(),
                        threadInfo.getThreadName(),
                        thread.getCpuPercentage(),
                        getTopStackLocation(
                                threadInfo
                        ),
                        incidentManager
                );
            }

            // =========================
            // GC
            // =========================

            RemoteGcMonitor gcMonitor =
                    new RemoteGcMonitor(connection);

            gcMonitor.printGcInformation();

            // =========================
            // CLASS LOADING
            // =========================

            RemoteClassLoadingMonitor classMonitor =
                    new RemoteClassLoadingMonitor(
                            connection
                    );

            classMonitor.printClassLoadingInformation();

            // =========================
            // RUNTIME
            // =========================

            RemoteRuntimeMonitor runtimeMonitor =
                    new RemoteRuntimeMonitor(
                            connection
                    );

            runtimeMonitor.printRuntimeInformation();

            // =========================
            // LOCKS
            // =========================

            RemoteLockMonitor lockMonitor =
                    new RemoteLockMonitor(connection);

            lockMonitor.printLockInformation();

            // =========================
            // DEADLOCK
            // =========================

            RemoteDeadlockMonitor deadlockMonitor =
                    new RemoteDeadlockMonitor(
                            connection
                    );

            deadlockMonitor.detectDeadlocks(
                    incidentManager
            );

            // =========================
            // STACK TRACES
            // =========================

            RemoteStackTraceMonitor stackMonitor =
                    new RemoteStackTraceMonitor(
                            connection
                    );

            stackMonitor.printStackTraces();

            // =========================
            // THREADS
            // =========================

            RemoteThreadMonitor threadMonitor =
                    new RemoteThreadMonitor();

            threadMonitor.showThreads(connection);

            // =========================
            // PRINT INCIDENTS
            // =========================

            incidentManager.printIncidents();

            // =========================
            // DISCONNECT
            // =========================

            jmx.disconnect();

        } catch (Exception e) {

            System.out.println(
                    "JMX monitoring failed."
            );

            e.printStackTrace();

        } finally {

            connector.detach();
        }
    }

    private static String getTopStackLocation(
            ThreadInfo threadInfo) {

        StackTraceElement[] stack =
                threadInfo.getStackTrace();

        if (stack.length == 0) {
            return "Unknown";
        }

        return stack[0].toString();
    }
}