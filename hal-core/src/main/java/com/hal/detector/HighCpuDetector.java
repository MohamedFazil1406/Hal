package com.hal.detector;

import com.hal.incident.Incident;
import com.hal.incident.IncidentManager;
import com.hal.incident.IncidentSeverity;
import com.hal.incident.IncidentType;

public class HighCpuDetector {

    private final double threshold;

    public HighCpuDetector(double threshold) {
        this.threshold = threshold;
    }

    public void check(
            long threadId,
            String threadName,
            double cpuUsage,
            String location,
            IncidentManager incidentManager) {

        if (cpuUsage < threshold) {
            return;
        }

        Incident incident =
                new Incident(
                        IncidentType.HIGH_CPU,
                        IncidentSeverity.WARNING,
                        "High CPU usage detected",
                        String.format(
                                "Thread is using %.2f%% CPU.",
                                cpuUsage
                        ),
                        threadName,
                        location
                );

        incidentManager.addIncident(incident);
    }
}