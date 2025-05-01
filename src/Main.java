import handlers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

         InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
         HttpServer server = HttpServer.create(address, 0);

         TaskManager manager = Managers.getTaskManager();


         Gson jsonMapper = new GsonBuilder()
                 .registerTypeAdapter(Instant.class, new InstantAdapter())
                 .registerTypeAdapter(Duration.class, new DurationAdapter())
                 .create();
         server.createContext("/tasks", new HttpTaskHandler(manager, jsonMapper));
         server.createContext("/epics", new HttpEpicHandler(manager, jsonMapper));
         server.createContext("/subtasks", new HttpSubtaskHandler(manager, jsonMapper));
         server.createContext("/history", new HttpHistoryHandler(manager, jsonMapper));
         server.createContext("//prioritized", new HttpPrioritizedHandler(manager, jsonMapper));

         server.start();
         System.out.println("HTTP-сервер запущен на " + PORT + " порту!");







        //Epic epic1 = new Epic("", "");
        //manager.createEpic(epic1);

        // Duration duration = Duration.ofMinutes(50);
        //Subtask subtask = new Subtask("","",Status.NEW, duration, timeConverter(timeFromUser), epic1.getId());
        // manager.createSubtask(subtask);
        //String timeFromUser = "01.01.2025 14:00";
        //Task task = new Task("Сделать ТЗ ", "Написать код", Status.NEW, Duration.ofMinutes(1), timeConverter(timeFromUser));

        //String timeFromUser2 = "01.01.2025 14:50";
        //Task task1 = new Task("Сделать ТЗ ", "Эндпоинты", Status.NEW, Duration.ofMinutes(4), timeConverter(timeFromUser2));

        //manager.createTask(task);
        //manager.createTask(task1);

         /*File file = File.createTempFile("backup", ".csv");
         FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

         TaskManager taskManager = new InMemoryTaskManager();
         String timeFromUser = "01.01.2025 14:00";
         Integer durationFromUser = 2;
         Instant startTime = timeConverter(timeFromUser);
         Task task = new Task("Сделать ТЗ ", "Написать код", Status.NEW, Duration.ofMinutes(1), startTime);
         Task newTask = fileBackedTaskManager.createTask(task);
         Task newTask1 = taskManager.createTask(task);
         Integer createdTaskId = newTask.getId();
         System.out.println(taskManager.getAllTasks());
         System.out.println(file);

         String timeFromUser1 = "01.01.2025 14:05";
         Task updatedTask = new Task("Сдел ТЗ", "Кодить", Status.NEW, Duration.ofMinutes(15), timeConverter(timeFromUser1));

         if (fileBackedTaskManager.updateTask(updatedTask)) {
              System.out.println("задача обновлена");
         }
         System.out.println(taskManager.getAllTasks());

         Epic newEpic1 = fileBackedTaskManager.createEpic(epic1);
         Integer createdEpicId = newEpic1.getId();
         Epic updatedEpic1 = new Epic("r","g");
         if (fileBackedTaskManager.updateTask(updatedEpic1)) {
              System.out.println("эпик обновлен");
         }

         Duration duration = Duration.ofMinutes(durationFromUser);
         Subtask subtask = new Subtask("","",Status.NEW, duration, startTime, createdEpicId);

         System.out.println(file);

         //File backup = new File("backup.csv");
         FileBackedTaskManager.loadFromFile(file);
         System.out.println(file); */

    }

     public static Instant timeConverter(String startTimeFromUser) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
          return LocalDateTime.parse(startTimeFromUser, formatter)
                  .atZone(ZoneId.systemDefault())
                  .toInstant();
     }


}
