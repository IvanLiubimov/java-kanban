package managers;

import task.Task;

import java.util.List;

public interface HistoryManager {
    void addToSeenTasks (Task task);
    List<Task> getSeenTasks();
}
