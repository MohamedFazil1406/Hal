package com.hal.incident;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ExceptionEventServer {

    private final int port;
    private ServerSocket serverSocket;

    public ExceptionEventServer(int port) {
        this.port = port;
    }

    public void start(IncidentManager incidentManager) {

        Thread serverThread =
                new Thread(
                        () -> listen(incidentManager),
                        "HAL-Exception-Server"
                );

        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void listen(
            IncidentManager incidentManager) {

        try {

            serverSocket =
                    new ServerSocket(port);

            System.out.println(
                    "Exception event server started on port "
                            + port
            );

            while (!serverSocket.isClosed()) {

                Socket socket =
                        serverSocket.accept();

                handleConnection(
                        socket,
                        incidentManager
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Exception event server stopped."
            );
        }
    }

    private void handleConnection(
            Socket socket,
            IncidentManager incidentManager) {

        try (
                socket;
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println();
                System.out.println(
                        "Received exception event:"
                );

                System.out.println(line);

                String[] parts =
                        line.split("\\|", -1);

                if (parts.length < 5) {
                    System.out.println(
                            "Invalid exception event."
                    );
                    continue;
                }

                String exceptionClass = parts[0];
                String message = parts[1];
                String className = parts[2];
                String methodName = parts[3];
                String location = parts[4];

                Incident incident =
                        new Incident(
                                IncidentType.EXCEPTION,
                                IncidentSeverity.ERROR,
                                exceptionClass,
                                message,
                                className + "." + methodName,
                                location
                        );

                incidentManager.addIncident(
                        incident
                );

                System.out.println();
                System.out.println(
                        "Exception incident created."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Failed to read exception event."
            );
        }
    }

    public void stop() {

        try {

            if (serverSocket != null) {
                serverSocket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}