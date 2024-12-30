import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> seenTasks = new ArrayList<>();

    @Override
    public void addToSeenTasks(Task seenTask) {
        if (seenTasks.size() >= 10) {
            seenTasks.remove(0);
            seenTasks.add(seenTask);
        }
        seenTasks.add(seenTask);

    }

    @Override
    public ArrayList<Task> getSeenTasks() {
        return seenTasks;
    }

}
