package com.hal.process;

import com.hal.jvm.JVMInfo;

import java.util.ArrayList;
import java.util.List;

public class JVMDiscovery {

    public List<JVMInfo> discover() {

        List<JVMInfo> jvms = new ArrayList<>();

        ProcessHandle.allProcesses()
                .forEach(process -> {

                    ProcessHandle.Info info = process.info();

                    String command = info.command().orElse("");

                    String commandLine =
                            info.commandLine().orElse("");

                    if (command.toLowerCase().contains("java")) {

                        JVMInfo jvm = new JVMInfo(
                                process.pid(),
                                command,
                                commandLine
                        );

                        jvms.add(jvm);
                    }
                });

        return jvms;
    }
}