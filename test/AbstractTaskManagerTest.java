import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractTaskManagerTest <M extends TaskManager> {

    protected M taskManager;

    abstract TaskManager getTaskManager();

    public static Instant timeConverter(String startTimeFromUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(startTimeFromUser, formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }


    @Test
    void createTaskTest() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Duration duration = Duration.ofMinutes(5);
        String timeFromUser = "01.01.2025 14:00";
        Task task = new Task(name, description, Status.NEW, duration, timeConverter(timeFromUser));

        taskManager.createTask(task);

        Task actualCreatedTask = taskManager.findTaskById(task.getId());
        System.out.println(task.getId());

        Assertions.assertTrue(actualCreatedTask.getId() > 0, "ID должен быть положительным");
        System.out.println(actualCreatedTask.getId());
        Assertions.assertEquals(description, actualCreatedTask.getDescription());
        Assertions.assertEquals(name, actualCreatedTask.getName());
        Assertions.assertEquals(Status.NEW, actualCreatedTask.getStatus());
        Assertions.assertEquals(Status.NEW, actualCreatedTask.getStatus());
    }


    @Test
    void differentIdsForEachTaskShouldNotBeEqual() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 15:57");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
        String name1 = "Сделать ДЗ";
        String description1 = "Спринт 5";
        Duration duration1 = Duration.ofMinutes(2);
        Instant startTime1 = timeConverter("01.01.2025 16:00");
        Task task1 = new Task (name1, description1, Status.NEW, duration1, startTime1);
        String name2 = "Сделать ДЗ";
        String description2 = "Спринт 5";
        Duration duration2 = Duration.ofMinutes(2);
        Instant startTime2 = timeConverter("01.01.2025 16:03");
        Task task2 = new Task (name2, description2, Status.NEW, duration2, startTime2);

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
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 16:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
        String name1 = "Сделать ДЗ";
        String description1 = "Спринт 5";
        Duration duration1 = Duration.ofMillis(2);
        Instant startTime1 = timeConverter("01.01.2025 16:03");
        Task task1 = new Task (name1, description1, Status.IN_PROGRESS, duration1, startTime1);

        Task createdTask = taskManager.createTask(task);
        Task createdTask1 = taskManager.createTask(task1);
        createdTask1.setId(createdTask.getId());

        Assertions.assertEquals(createdTask,createdTask1);

    }

    @Test
    void epicsOrSubtasksShouldBeEqualIfSameId(){
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 16:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
        Task createdTask = taskManager.createTask(task);
        String name1 = "Сделать";
        String description1 = "Спринт";
        Epic epic = new Epic (name1, description1);
        Epic createdEpic = taskManager.createEpic(epic);
        String name2 = "Тест";
        String description2 = "Тест";
        Duration duration2 = Duration.ofMinutes(2);
        Instant startTime2 = timeConverter("01.01.2025 16:06");
        Subtask subtask = new Subtask (name2, description2, Status.NEW, duration2, startTime2, createdEpic.getId());
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
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 16:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
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
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 16:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);

        String name1 = "Тест";
        String description1 = "Тест";
        Duration duration1 = Duration.ofMinutes(2);
        Instant startTime1 = timeConverter("01.01.2025 16:03");
        Task task1 = new Task (name1, description1, Status.NEW, duration1, startTime1);

        Task createdTask = taskManager.createTask(task);
        Task createdTask1 = taskManager.createTask(task1);
        createdTask1.setId(createdTask.getId());

        assertTrue(taskManager.isIdConflict(task1.getId()));
    }

    @Test
    void tasksAddedToHistoryManagerShouldSavePreviousTasksData() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 17:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
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
    void EpicStatusUpdate() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Epic epic = new Epic(name, description);
        taskManager.createEpic(epic);
        String name1 = "1";
        String description1 = "2";
        Duration duration1 = Duration.ofMinutes(2);
        Instant startTime1 = timeConverter("01.01.2025 17:00");
        Subtask subtask = new Subtask(name1, description1,Status.NEW, duration1,startTime1, epic.getId());
        String name2 = "3";
        String description2 = "4";
        Duration duration2 = Duration.ofMinutes(2);
        Instant startTime2 = timeConverter("01.01.2025 17:04");
        Subtask subtask2 = new Subtask(name2, description2,Status.NEW, duration2,startTime2, epic.getId());
        String name3 = "5";
        String description3 = "6";
        Duration duration3 = Duration.ofMinutes(2);
        Instant startTime3 = timeConverter("01.01.2025 17:07");
        Subtask subtask3 = new Subtask(name3, description3,Status.NEW, duration3,startTime3, epic.getId());
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        epic.updateDurationByTasks();
        epic.updateStartTimeByTasks();
        epic.updateStatusByTasks();

        Assertions.assertEquals(epic.getStatus(), Status.NEW);

        subtask.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        epic.updateStatusByTasks();

        Assertions.assertEquals(epic.getStatus(), Status.DONE);

        subtask.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        subtask3.setStatus(Status.DONE);
        epic.updateStatusByTasks();

        Assertions.assertEquals(epic.getStatus(), Status.NEW);

        subtask.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);
        epic.updateStatusByTasks();

        Assertions.assertEquals(epic.getStatus(), Status.NEW);

    }

    @Test
    void epicDurationUpdateTest (){
        String name1 = "1";
        String description1 = "2";
        Epic epic = new Epic (name1, description1);
        taskManager.createEpic(epic);

        String name2 = "3";
        String description2 = "4";
        Duration duration2 = Duration.ofMinutes(7);
        Instant startTime2 = timeConverter("01.01.2025 16:00");
        Subtask subtask = new Subtask (name2, description2, Status.NEW, duration2, startTime2, epic.getId());
        taskManager.createSubtask(subtask);

        String name3 = "5";
        String description3 = "6";
        Duration duration3 = Duration.ofMinutes(2);
        Instant startTime3 = timeConverter("01.01.2025 15:38");
        Subtask subtask1 = new Subtask (name3, description3, Status.NEW, duration3, startTime3, epic.getId());
        taskManager.createSubtask(subtask1);

        epic.updateDurationByTasks();
        epic.updateStartTimeByTasks();

        System.out.println(LocalDateTime.ofInstant(epic.getStartTime(), ZoneId.systemDefault()));
        System.out.println(epic.getDuration().toMinutes());

        Assertions.assertEquals(epic.getStartTime(), subtask1.getStartTime());
        Assertions.assertEquals(epic.getDuration(), subtask.getDuration().plus(subtask1.getDuration()));
        Assertions.assertEquals(epic.getEndTime(), subtask.getEndTime());
    }

    @Test
    void TasksHasNoInteractions() {
        String name = "Сделать ДЗ";
        String description = "Спринт 5";
        Duration duration = Duration.ofMinutes(2);
        Instant startTime = timeConverter("01.01.2025 16:00");
        Task task = new Task (name, description, Status.NEW, duration, startTime);
        taskManager.createTask(task);

        String name1 = "1";
        String description1 = "2";
        Epic epic = new Epic (name1, description1);
        taskManager.createEpic(epic);

        String name2 = "3";
        String description2 = "4";
        Duration duration2 = Duration.ofMinutes(2);
        Instant startTime2 = timeConverter("01.01.2025 16:03");
        Subtask subtask = new Subtask (name2, description2, Status.NEW, duration2, startTime2, epic.getId());
        taskManager.createSubtask(subtask);

        String name3 = "5";
        String description3 = "6";
        Duration duration3 = Duration.ofMinutes(2);
        Instant startTime3 = timeConverter("01.01.2025 16:06");
        Subtask subtask1 = new Subtask (name3, description3, Status.NEW, duration3, startTime3, epic.getId());
        taskManager.createSubtask(subtask1);

        epic.updateStatusByTasks();
        epic.updateDurationByTasks();
        epic.updateStartTimeByTasks();
            System.out.println(taskManager.getPrioritizedTasks());

            System.out.println(taskManager.getPrioritizedTasks());


    }


            @Test
            void shouldThrowExceptionWhenTasksIntersect() {

                Task task1 = new Task("1", "1", Status.NEW, Duration.ofMinutes(60), Instant.parse("2025-04-27T14:00:00Z"));
                Task task2 = new Task("2", "2", Status.NEW, Duration.ofMinutes(60), Instant.parse("2025-04-27T14:30:00Z")); // пересекается с task1

                taskManager.createTask(task1);


                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                    taskManager.createTask(task2);
                });

                assertEquals("Нельзя создать задачу: время выполнения пересекается с другой задачей", exception.getMessage());
            }
        }