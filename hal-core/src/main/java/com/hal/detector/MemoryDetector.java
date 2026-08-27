package com.hal.detector;

import com.hal.incident.Incident;
import com.hal.incident.IncidentManager;
import com.hal.incident.IncidentSeverity;
import com.hal.incident.IncidentType;

public class MemoryDetector {

    private final double threshold;

    public MemoryDetector(double threshold) {
        this.threshold = threshold;
    }

    public void check(
            long usedMemory,
            long maxMemory,
            IncidentManager incidentManager) {

        if (maxMemory <= 0) {
            return;
        }

        double usagePercentage =
                (usedMemory / (double) maxMemory) * 100.0;

        if (usagePercentage < threshold) {
            return;
        }

        Incident incident =
                new Incident(
                        IncidentType.HIGH_MEMORY,
                        IncidentSeverity.WARNING,
                        "High memory usage detected",
                        String.format(
                                "JVM heap usage is %.2f%%.",
                                usagePercentage
                        ),
                        "JVM Heap",
                        "Memory"
                );

        incidentManager.addIncident(incident);
    }
}