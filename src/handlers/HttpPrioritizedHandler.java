package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ErrorResponse;
import managers.TaskManager;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class HttpPrioritizedHandler extends  BaseHttpHandler {
    private TaskManager taskManager;
    private Gson jsonMapper;

    public HttpPrioritizedHandler(TaskManager taskManager, Gson jsonMapper) {
        this.taskManager = taskManager;
        this.jsonMapper = jsonMapper;
    }


    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            String json = jsonMapper.toJson(prioritizedTasks);
            sendText(exchange, json, 200);
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                default:
                    ErrorResponse errorResponse = new ErrorResponse("Обработка данного метода не предусмотрена",
                            405, exchange.getRequestURI().getPath());
                    String jsonText = jsonMapper.toJson(errorResponse);
                    sendText(exchange, jsonText, 405);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500, exchange.getRequestURI().getPath());
            String jsonText = jsonMapper.toJson(errorResponse);
            sendText(exchange, jsonText, 500);
        } finally {
            exchange.close();
        }

    }
}

