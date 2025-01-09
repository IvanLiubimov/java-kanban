package managers;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory ();

    Task createTask(Task newTask);

    boolean updateTask(Task task);

    Task deleteTaskById(Integer id);

    List<Task> getAllTasks();

    Task findTaskById(Integer id);

    void deleteAllTasks();

    //////////////////////////
    Epic createEpic(Epic newEpic);

    Epic deleteEpicById(Integer id);

    boolean updateEpic(Epic epic);

    List<Epic> getAllEpic();

    Epic findEpicById(Integer id);

    void deleteAllEpics();

    List<Subtask> getSubtaskByEpic(int epicId);

    ///////////////////////
    Subtask createSubtask(Subtask newSubtask);

    boolean updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);

    List<Subtask> getAllSubtasks();

    void deteteAllSubtusks();

    Subtask findSubtaskById(Integer id);

    public boolean isIdConflict(int id);
}
