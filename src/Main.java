import exceptions.FileManagerRecoveryException;
import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws IOException, FileManagerRecoveryException {

         File file = File.createTempFile("backup", ".csv");
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
         Epic epic1 = new Epic("", "");
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
         //FileBackedTaskManager.loadFromFile(backup);
         //System.out.println(backup);

         fileBackedTaskManager.deleteTaskById(createdTaskId);
         fileBackedTaskManager.deleteTaskById(createdTaskId);

    }

     public static Instant timeConverter(String startTimeFromUser) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
          return LocalDateTime.parse(startTimeFromUser, formatter)
                  .atZone(ZoneId.systemDefault())
                  .toInstant();
     }


}
