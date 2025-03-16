import Exceptions.FileManagerFileRecoveryException;
import managers.FileBackedTaskManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FileManagerFileRecoveryException {

         File file = File.createTempFile("backup", ".csv");
         FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file, new InMemoryHistoryManager());


         TaskManager taskManager = new InMemoryTaskManager();
         Task task = new Task("Сделать ТЗ ", "Написать код", Status.NEW);
         Task newTask = fileBackedTaskManager.createTask(task);
         Integer createdTaskId = newTask.getId();
         Task updatedTask = new Task("Сдел ТЗ", "Кодить", Status.NEW);
         if (fileBackedTaskManager.updateTask(updatedTask)) {
              System.out.println("задача обновлена");
         }
         System.out.println(taskManager.getAllTasks());
         Epic epic1 = new Epic("", "");
         Epic newEpic1 = fileBackedTaskManager.createEpic(epic1);
         Integer createdEpic = newEpic1.getId();
         Epic updatedEpic1 = new Epic("r","g");
         if (fileBackedTaskManager.updateTask(updatedEpic1)) {
              System.out.println("эпик обновлен");
         }
         Subtask subtask = new Subtask("","",Status.NEW, 4);

         System.out.println(file);

         File backup = new File("backup.csv");
         FileBackedTaskManager.loadFromFile(backup);
         System.out.println(backup);

         fileBackedTaskManager.deleteTaskById(createdTaskId);
         fileBackedTaskManager.deleteTaskById(createdTaskId);





    }
}
