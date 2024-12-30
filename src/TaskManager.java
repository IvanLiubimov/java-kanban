import java.util.ArrayList;

public interface TaskManager {

    Task createTask(Task newTask);

    boolean updateTask(Task task);

    Task deleteTaskById(Integer id);

    ArrayList<Task> getAllTasks();

    Task findTaskById(Integer id);

    public ArrayList <Task> getSeenTasks();

    void deleteAllTasks();

    //////////////////////////
    Epic createEpic(Epic newEpic);

    Epic deleteEpicById(Integer id);

    boolean updateEpic(Epic epic);

    ArrayList<Epic> getAllEpic();

    Epic findEpicById(Integer id);

    void deleteAllEpics();

    ArrayList<Subtask> getSubtaskByEpic(int epicId);

    ///////////////////////
    Subtask createSubtask(Subtask newSubtask);

    boolean updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);

    ArrayList<Subtask> getAllSubtasks();

    void deteteAllSubtusks();

    Subtask findSubtaskById(Integer id);

    public boolean isIdConflict(int id);
}
