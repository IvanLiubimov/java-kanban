import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTests extends AbstractTaskManagerTest {

    @BeforeEach
    void init () {
        taskManager = getTaskManager();
    }

    @Override
    TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }
}
