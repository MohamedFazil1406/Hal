package com.hal.detector;

import com.hal.incident.Incident;
import com.hal.incident.IncidentManager;
import com.hal.incident.IncidentSeverity;
import com.hal.incident.IncidentType;

public class LockContentionDetector {

    public void check(
            long threadId,
            String threadName,
            String lockName,
            String lockOwnerName,
            IncidentManager incidentManager) {

        if (lockName == null) {
            return;
        }

        Incident incident =
                new Incident(
                        IncidentType.LOCK_CONTENTION,
                        IncidentSeverity.WARNING,
                        "Thread blocked on lock",
                        "Thread is blocked waiting for a lock owned by "
                                + lockOwnerName
                                + ".",
                        threadName,
                        lockName
                );

        incidentManager.addIncident(incident);
    }
}