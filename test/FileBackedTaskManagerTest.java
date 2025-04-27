import exceptions.FileManagerRecoveryException;
import managers.FileBackedTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends AbstractTaskManagerTest{

    @BeforeEach
    void init () {
        taskManager = getTaskManager();
    }

    @Override
    FileBackedTaskManager getTaskManager() {
        try {
            File tempFile = File.createTempFile("test", ".csv");
            return new FileBackedTaskManager(tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadFromFileRestoresAllEntities() throws Exception, FileManagerRecoveryException {

        String timeFromUser1 = "01.01.2025 14:05";
        Task task = new Task("Сдел ТЗ", "Кодить", Status.NEW, Duration.ofMinutes(15),
                timeConverter(timeFromUser1));
        Epic e1 = new Epic("E1", "Ed1");
        e1.setId(taskManager.createEpic(e1).getId() );
        String timeFromUser2 = "01.01.2025 15:15";
        Subtask subtask = new Subtask("S1", "Sd1", Status.NEW, Duration.ofMinutes(15),
                timeConverter(timeFromUser2),
                e1.getId());

        taskManager.createTask(task);
        taskManager.createSubtask(subtask);

        File file = ((FileBackedTaskManager) taskManager).getData();
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(file);

        List<Task> originalTasks    = List.of(task);
        List<Subtask> originalSubtasks = List.of(subtask);

        assertEquals(originalTasks,    loaded.getAllTasks());
        assertEquals(originalSubtasks, loaded.getAllSubtasks());

    }




}
