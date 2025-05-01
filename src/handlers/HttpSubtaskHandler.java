package handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ErrorResponse;
import exceptions.SubtaskNotFoundException;
import managers.TaskManager;
import task.Status;
import task.Subtask;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class HttpSubtaskHandler extends BaseHttpHandler {
    private TaskManager taskManager;
    private Gson jsonMapper;

    public HttpSubtaskHandler(TaskManager taskManager, Gson jsonMapper) {
        this.taskManager = taskManager;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        try {
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    ErrorResponse errorResponse = new ErrorResponse("Обработка данного метода не предусмотрена",
                            405, exchange.getRequestURI().getPath());
                    String jsonText = jsonMapper.toJson(errorResponse);
                    sendText(exchange, jsonText, 405);
            }
        } catch (SubtaskNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String jsonText = jsonMapper.toJson(errorResponse);
            sendText(exchange, jsonText, 404);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 406, exchange.getRequestURI().getPath());
            String jsonText = jsonMapper.toJson(errorResponse);
            sendText(exchange, jsonText, 406);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500, exchange.getRequestURI().getPath());
            String jsonText = jsonMapper.toJson(errorResponse);
            sendText(exchange, jsonText, 500);
        } finally {
            exchange.close();
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteSubtaskById(id);
            sendText(exchange, "Подзадача_с_id_" + id + "_удалена_:", 200);
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");


        if (pathParts.length == 8) {
            Status status = statusReader(pathParts[4]);
            Instant startTime = timeConverter(pathParts[6]);
            int epicId = Integer.parseInt(pathParts[7]);
            Subtask subtask = new Subtask(pathParts[2], pathParts[3], status, Duration.ofMinutes(Integer.parseInt(pathParts[5])),
                    startTime, epicId);
            taskManager.createSubtask(subtask);
            String jsonText = jsonMapper.toJson(subtask.toString());
            sendText(exchange, "Подзадача_создана_:" + jsonText, 201);
            //ТЗ 9/Эндпоинты/3/01.01.2025 14:05/1
        }
        if (pathParts.length == 9) {
            int id = Integer.parseInt(pathParts[2]);
            Status status = statusReader(pathParts[5]);
            Instant startTime = timeConverter(pathParts[6]);
            int epicId = Integer.parseInt(pathParts[6]);
            Subtask subtask = new Subtask(pathParts[3], pathParts[4], status, Duration.ofMinutes(Integer.parseInt(pathParts[5])),
                    startTime, epicId);
            subtask.setId(id);
            taskManager.updateTask(subtask);
            String jsonText = jsonMapper.toJson(subtask.toString());
            sendText(exchange, "Подзадача_с_id_" + id + "_обновлена_:" + jsonText, 201);
            //3/ТЗ 9/Эндпоинты/3/01.01.2025 14:05/1
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            List<Subtask> allSubtasks = taskManager.getAllSubtasks();
            String json = jsonMapper.toJson(allSubtasks);
            System.out.println(json);
            sendText(exchange, json, 200);
        }
        if (pathParts.length == 3) {
            Integer id = Integer.valueOf(pathParts[2]);
            Subtask subtaskById = taskManager.findSubtaskById(id);
            String json = jsonMapper.toJson(subtaskById);
            System.out.println(json);
            sendText(exchange, json, 200);
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }
}
