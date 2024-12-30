public class Managers {
    public static TaskManager getTaskManager () {
        HistoryManager historyManager = new InMemoryHistoryManager();

        return new InMemoryTaskManager(historyManager);
    }
}
