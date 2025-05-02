import handlers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.*;
import server.TaskServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager taskManager = new InMemoryTaskManager();
        TaskServer taskServer = new TaskServer(taskManager);

        taskServer.start();

        taskServer.stop(300);







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
