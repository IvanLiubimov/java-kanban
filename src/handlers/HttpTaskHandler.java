package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.ErrorResponse;
import exceptions.TaskNotFoundException;
import managers.TaskManager;
import task.Status;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class HttpTaskHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private Gson jsonMapper;

    public HttpTaskHandler(TaskManager taskManager, Gson jsonMapper) {
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
        } catch (TaskNotFoundException e) {
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
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }


        exchange.getResponseBody().write("Hello".getBytes(StandardCharsets.UTF_8));
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().flush();
        exchange.close();

    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteTaskById(id);
            sendText(exchange, "задача_с_id_" + id + "_удалена_:", 200);
        } else {
        throw new IllegalArgumentException("Неверный формат запроса");
    }

    }

    private void handlePost(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");
        Status status = Status.NEW;

        if (pathParts.length == 6) {

            Instant startTime = timeConverter(pathParts[5]);
            Task task = new Task(pathParts[2], pathParts[3], status, Duration.ofMinutes(Integer.parseInt(pathParts[4])),
                    startTime);
            taskManager.createTask(task);
            String jsonText = jsonMapper.toJson(task.toString());
            sendText(exchange, "Эадача_создана_:" + jsonText, 201);
            //Сделать ТЗ/Написать код/3/01.01.2025 14:00

        }
        if (pathParts.length == 7) {
            int id = Integer.parseInt(pathParts[2]);
            Instant startTime = timeConverter(pathParts[6]);
            Task task = new Task(pathParts[3], pathParts[4], status, Duration.ofMinutes(Integer.parseInt(pathParts[5])),
                    startTime);
            task.setId(id);
            taskManager.updateTask(task);
            String jsonText = jsonMapper.toJson(task.toString());
            sendText(exchange, "задача_с_id_" + id + "_обновлена_:" + jsonText, 201);
                //1/ТЗ 9/Эндпоинты/3/01.01.2025 14:05
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }



    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            List<Task> allTasks = taskManager.getAllTasks();
            String json = jsonMapper.toJson(allTasks);
            System.out.println(json);
            sendText(exchange, json, 200);
            System.out.println();
        }
        if (pathParts.length == 3) {
            Integer id = Integer.valueOf(pathParts[2]);
            Task taskById = taskManager.findTaskById(id);
            String json = jsonMapper.toJson(taskById);
            System.out.println(json);
            sendText(exchange, json, 200);
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }
}


