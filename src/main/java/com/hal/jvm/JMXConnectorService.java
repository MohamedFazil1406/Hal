package com.hal.jvm;

import com.sun.tools.attach.VirtualMachine;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXConnectorService {

    private JMXConnector connector;

    public MBeanServerConnection connect(VirtualMachine vm)
            throws Exception {

        System.out.println("Starting JMX management agent...");

        String connectorAddress =
                vm.startLocalManagementAgent();

        if (connectorAddress == null ||
                connectorAddress.isBlank()) {

            throw new IllegalStateException(
                    "Could not obtain JMX connector address"
            );
        }

        System.out.println("JMX URL obtained.");

        JMXServiceURL serviceURL =
                new JMXServiceURL(connectorAddress);

        System.out.println("Connecting to JMX...");

        connector =
                JMXConnectorFactory.connect(serviceURL);

        System.out.println("JMX connected!");

        return connector.getMBeanServerConnection();
    }

    public void disconnect() {

        if (connector != null) {

            try {

                connector.close();

                System.out.println(
                        "JMX disconnected."
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}