package com.hal.jvm;

import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.util.Properties;

public class JVMConnector {

    private VirtualMachine vm;

    public VirtualMachine attach(long pid) {

        try {

            System.out.println(
                    "Connecting to JVM with PID: " + pid
            );

            vm = VirtualMachine.attach(
                    String.valueOf(pid)
            );

            System.out.println(
                    "Successfully connected!"
            );

            return vm;

        } catch (Exception e) {

            System.out.println(
                    "Failed to connect to JVM."
            );

            e.printStackTrace();

            return null;
        }
    }

    public void detach() {

        if (vm != null) {

            try {

                vm.detach();

                System.out.println(
                        "Disconnected from JVM."
                );

                vm = null;

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}