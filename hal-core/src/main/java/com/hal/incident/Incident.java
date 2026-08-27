package com.hal.incident;

public class Incident {

    private final IncidentType type;
    private final IncidentSeverity severity;
    private final String title;
    private final String description;
    private final String threadName;
    private final String location;

    public Incident(
            IncidentType type,
            IncidentSeverity severity,
            String title,
            String description,
            String threadName,
            String location) {

        this.type = type;
        this.severity = severity;
        this.title = title;
        this.description = description;
        this.threadName = threadName;
        this.location = location;
    }

    public IncidentType getType() {
        return type;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getLocation() {
        return location;
    }

    public void print() {

        System.out.println();
        System.out.println("===== HAL INCIDENT =====");

        System.out.println(
                "Type: " + type
        );

        System.out.println(
                "Severity: " + severity
        );

        System.out.println(
                "Title: " + title
        );

        System.out.println(
                "Description: " + description
        );

        System.out.println(
                "Thread: " + threadName
        );

        System.out.println(
                "Location: " + location
        );
    }
}