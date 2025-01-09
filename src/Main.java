import managers.InMemoryTaskManager;
import managers.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
         TaskManager taskManager = new InMemoryTaskManager();
         Task task = new Task("Сделать ТЗ ", "Написать код", Status.NEW);
         Task newTask = taskManager.createTask(task);
         Integer createdTaskId = newTask.getId();
         Task updatedTask = new Task ("Сдел ТЗ", "Кодить", Status.NEW);
         if (taskManager.updateTask(updatedTask)) {
              System.out.println("задача обновлена");
         }
         System.out.println(taskManager.getAllTasks());
         Epic epic1 = new Epic("", "");
         Epic newEpic1 = taskManager.createEpic(epic1);
         Epic updatedEpic1 = new Epic ("r","g");
         if (taskManager.updateTask(updatedEpic1)) {
              System.out.println("эпик обновлен");
         }
         Subtask subtask = new Subtask("","",Status.NEW, 4);


    }
}
