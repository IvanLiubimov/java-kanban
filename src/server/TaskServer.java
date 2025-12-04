package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;

public class TaskServer {
    private static final int PORT = 8080;

    private final HttpServer server;

    public TaskServer(TaskManager manager) throws IOException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", PORT);
        this.server = HttpServer.create(address, 0);

        Gson jsonMapper = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        server.createContext("/tasks", new HttpTaskHandler(manager, jsonMapper));
        server.createContext("/epics", new HttpEpicHandler(manager, jsonMapper));
        server.createContext("/subtasks", new HttpSubtaskHandler(manager, jsonMapper));
        server.createContext("/history", new HttpHistoryHandler(manager, jsonMapper));
        server.createContext("/prioritized", new HttpPrioritizedHandler(manager, jsonMapper));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на порту " + PORT);
    }

    public void stop(int delaySeconds) {
        server.stop(delaySeconds);
        System.out.println("HTTP-сервер остановлен.");
    }
}

