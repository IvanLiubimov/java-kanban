import java.util.ArrayList;

public interface HistoryManager {
    public void addToSeenTasks (Task task);
    public ArrayList<Task> getSeenTasks();
}
