package com.hal.detector;

import com.hal.incident.Incident;
import com.hal.incident.IncidentManager;
import com.hal.incident.IncidentSeverity;
import com.hal.incident.IncidentType;

public class ExceptionDetector {

    public void report(
            String exceptionType,
            String message,
            String threadName,
            String location,
            IncidentManager incidentManager) {

        String description;

        if (message == null || message.isBlank()) {

            description =
                    "Exception "
                            + exceptionType
                            + " was thrown.";

        } else {

            description =
                    "Exception "
                            + exceptionType
                            + " was thrown: "
                            + message;
        }

        Incident incident =
                new Incident(
                        IncidentType.EXCEPTION,
                        IncidentSeverity.CRITICAL,
                        "Application exception detected",
                        description,
                        threadName,
                        location
                );

        incidentManager.addIncident(
                incident
        );
    }
}