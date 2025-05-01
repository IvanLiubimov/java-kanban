package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.StatusIsWrongException;
import task.Status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class BaseHttpHandler implements HttpHandler {

        protected void sendText(HttpExchange h, String text, Integer code) throws IOException {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        }

    protected void sendNotFound(HttpExchange h, String text, Integer code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions (HttpExchange h, String text, Integer code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }


    protected static Instant timeConverter(String startTimeFromUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(startTimeFromUser, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }

    protected Status statusReader (String status) {
        return switch (status) {
            case "NEW" -> Status.NEW;
            case "DONE" -> Status.DONE;
            case "IN_PROCESS" -> Status.IN_PROGRESS;
            default -> throw new StatusIsWrongException("Неверно указан статус выполнения задачи");
        };
    }

}



