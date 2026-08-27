package com.hal.incident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncidentManager {

    private final List<Incident> incidents =
            new ArrayList<>();

    public void addIncident(Incident incident) {

        if (incident == null) {
            return;
        }

        incidents.add(incident);
    }

    public List<Incident> getIncidents() {

        return Collections.unmodifiableList(
                incidents
        );
    }

    public int getIncidentCount() {

        return incidents.size();
    }

    public void printIncidents() {

        System.out.println();
        System.out.println("===== HAL INCIDENTS =====");

        if (incidents.isEmpty()) {

            System.out.println(
                    "No incidents detected."
            );

            return;
        }

        System.out.println(
                "Total Incidents: "
                        + incidents.size()
        );

        for (Incident incident : incidents) {
            incident.print();
        }
    }

    public void clear() {

        incidents.clear();
    }
}