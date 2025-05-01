package Handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exceptions.EpicNotFoundException;
import exceptions.ErrorResponse;
import managers.TaskManager;
import task.Epic;
import task.Subtask;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HttpEpicHandler extends BaseHttpHandler {
    private TaskManager taskManager;
    private Gson jsonMapper;

    public HttpEpicHandler(TaskManager taskManager, Gson jsonMapper) {
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
        } catch (EpicNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 404, exchange.getRequestURI().getPath());
            String jsonText = jsonMapper.toJson(errorResponse);
            sendText(exchange, jsonText, 404);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            taskManager.deleteEpicById(id);
            sendText(exchange, "Эпик_с_id_" + id + "_удалена_:", 200);
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }

    }

    private void handlePost(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 4) {
            Epic epic = new Epic(pathParts[2], pathParts[3]);
            taskManager.createEpic(epic);
            String jsonText = jsonMapper.toJson(epic.toString());
            sendText(exchange, "Эпик_создан_:" + jsonText, 201);
            //Сделать ТЗ/Написать код/3/01.01.2025 14:00
        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            List<Epic> allEpic = taskManager.getAllEpic();
            String json = jsonMapper.toJson(allEpic);
            System.out.println(json);
            sendText(exchange, json, 200);
        } if (pathParts.length == 3) {
            Integer id = Integer.valueOf(pathParts[2]);
            Epic epicById = taskManager.findEpicById(id);
            String json = jsonMapper.toJson(epicById);
            System.out.println(json);
            sendText(exchange, json, 200);
        } if (pathParts.length == 4) {
            int id = Integer.parseInt(pathParts[2]);
            List<Subtask> epicSubtasks = taskManager.getSubtaskByEpic(id);
            String json = jsonMapper.toJson(epicSubtasks);
            System.out.println(json);
            sendText(exchange, json, 200);

        } else {
            throw new IllegalArgumentException("Неверный формат запроса");
        }
    }
    }