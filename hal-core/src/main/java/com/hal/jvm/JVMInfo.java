package com.hal.jvm;

public class JVMInfo {

    private final long pid;
    private final String command;
    private final String commandLine;

    public JVMInfo(long pid, String command, String commandLine) {
        this.pid = pid;
        this.command = command;
        this.commandLine = commandLine;
    }

    public long getPid() {
        return pid;
    }

    public String getCommand() {
        return command;
    }

    public String getCommandLine() {
        return commandLine;
    }

    @Override
    public String toString() {

        return "JVMInfo{" +
                "pid=" + pid +
                ", command='" + command + '\'' +
                ", commandLine='" + commandLine + '\'' +
                '}';
    }
}