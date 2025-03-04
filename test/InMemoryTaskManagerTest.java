import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeAll
    public static void createNewTaskManager () {
        taskManager = Managers.getTaskManager();
    }


    @Test
    void createTask() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);

        taskManager.createTask(task);

        Task actualCreatedTask = taskManager.findTaskById(task.getId());
        Assertions.assertNotNull(actualCreatedTask.getId());
        Assertions.assertEquals(actualCreatedTask.getDescription(), description);
        Assertions.assertEquals(actualCreatedTask.getName(), name);
        Assertions.assertEquals(actualCreatedTask.getStatus(), Status.NEW);
    }

    @Test
    void differentIdsForEachTaskShouldNotBeEqual() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);
        String name1 = "Сделать ДЗ";
        String description1 = "Спринт 5";
        Task task1 = new Task (name1, description1, Status.NEW);
        String name2 = "Сделать ДЗ";
        String description2 = "Спринт 5";
        Task task2 = new Task (name2, description2, Status.NEW);

        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertNotEquals(task.getId(), task1.getId());
        assertNotEquals(task.getId(), task2.getId());
        assertNotEquals(task1.getId(), task2.getId());
    }


    @Test
    void tasksShouldBeEqualIfSameId(){
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);
        String name1 = "Тест";
        String description1 = "Тест";
        Task task1 = new Task (name1, description1, Status.IN_PROGRESS);

        Task createdTask = taskManager.createTask(task);
        Task createdTask1 = taskManager.createTask(task1);
        createdTask1.setId(createdTask.getId());

        Assertions.assertEquals(createdTask,createdTask1);

    }

    @Test
    void epicsOrSubtasksShouldBeEqualIfSameId(){
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);
        Task createdTask = taskManager.createTask(task);
        String name1 = "Сделать";
        String description1 = "Спринт";
        Epic epic = new Epic (name1, description1);
        Epic createdEpic = taskManager.createEpic(epic);
        String name2 = "Тест";
        String description2 = "Тест";
        Subtask subtask = new Subtask (name2, description2, Status.NEW, createdEpic.getId());
        Subtask createdSubtask = taskManager.createSubtask(subtask);

        createdTask.setId(1);
        createdEpic.setId(1);
        createdSubtask.setId(1);

        Assertions.assertEquals(createdTask,createdSubtask);
        Assertions.assertEquals(createdTask,createdEpic);

    }
    @Test
    void classManagersShouldReturnReadyManager (){
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);
        TaskManager manager1 = Managers.getTaskManager();
        TaskManager manager2 = Managers.getTaskManager();
        TaskManager manager3 = Managers.getTaskManager();

        Task createdTask1 = manager1.createTask(task);
        Task createdTask2 = manager2.createTask(task);
        Task createdTask3 = manager3.createTask(task);

        assertNotNull(createdTask1);
        assertNotNull(createdTask2);
        assertNotNull(createdTask3);

    }

    @Test
    void tasksWithSetIdShouldNotConflictWithGeneratedId() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);

        String name1 = "Тест";
        String description1 = "Тест";
        Task task1 = new Task (name1, description1, Status.IN_PROGRESS);

        Task createdTask = taskManager.createTask(task);
        Task createdTask1 = taskManager.createTask(task1);
        createdTask1.setId(createdTask.getId());
        taskManager.createTask(task);
        assertTrue(taskManager.isIdConflict(task1.getId()));

    }

    @Test
    void taskShouldBeUnchangeable () {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);


        Task unchangebleTask = taskManager.createTask(task);
        task.setName("ДЗ");
        task.setDescription("C 7");
        task.setStatus(Status.DONE);
        Task taskAfterChanges = taskManager.createTask(task);

        Assertions.assertEquals(unchangebleTask.getName(), taskAfterChanges.getName());
        Assertions.assertEquals(unchangebleTask.getDescription(), taskAfterChanges.getDescription());
        Assertions.assertEquals(unchangebleTask.getStatus(), taskAfterChanges.getStatus());

    }

    @Test
    void tasksAddedToHistoryManagerShouldSavePreviousTasksData() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Task task = new Task (name, description, Status.NEW);

        taskManager.createTask(task);
        taskManager.findTaskById(task.getId());
        Task seenTaskBeforeUpdate = taskManager.getHistory().get(0);
        String nameOfSeenTask = seenTaskBeforeUpdate.getName();
        task.setName("ДЗ");
        taskManager.updateTask(task);
        Task seenTaskAfterUpdate = taskManager.getHistory().get(0);

        Assertions.assertEquals(nameOfSeenTask, seenTaskAfterUpdate.getName());



    }

    @Test
    void epicShouldBeAddedToHistoryList() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Epic epic = new Epic (name, description);
        HistoryManager history = new InMemoryHistoryManager();
        Epic epicToSeen = taskManager.createEpic(epic);

        taskManager.findEpicById(epicToSeen.getId());
        List <Task >historyList = history.getSeenTasks();
        System.out.println(historyList);

        assertTrue(historyList.contains(epicToSeen));

    }
}