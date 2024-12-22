import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap <Integer, Task> tasks;
    private HashMap <Integer, Epic> epics;
    private HashMap <Integer, Subtask> subtasks;
    private int idCounter = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public Task createTask(Task newTask) {
        int newId = idGenerator();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;

    }
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            Task existingTask = tasks.get(task.getId());
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return true;
        }
        return false;

    }

    public Task deleteTaskById(Integer id) {
        return tasks.remove(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());

    }

    public Task findTaskById(Integer id) {
        return tasks.get(id);

    }

    public void deleteAllTasks() {
        tasks.clear();
    }
//////////////////////////
    public Epic createEpic(Epic newEpic) {
        int newId = idGenerator();
        newEpic.setId(newId);
        epics.put (newEpic.getId(), newEpic);
        return newEpic;

    }

    public Epic deleteEpicById(Integer id) { // 1) есть ли айди 2) залезай в талицу сабтасков и удаляй сабтаски
        if (epics.containsKey(id)) {
            Epic existingEpic = epics.get(id);
            ArrayList <Subtask> epicSubtasks = existingEpic.getSubtasks();
            for (Subtask subtask : epicSubtasks) {
                Integer idToDelete = subtask.getId();
                subtasks.remove(idToDelete);
            }
            return epics.remove(id);
        }
        return null;
    }

    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            return true;
        }
        return false;// проверять все подзадачи
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());


    }

    public Epic findEpicById(Integer id) {
        return epics.get(id);
    }

    public void deleteAllEpics () {
        subtasks.clear();
        epics.clear();
    }

    public ArrayList<Subtask> getSubtaskByEpic (int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Subtask> allEpicSubtasks = epic.getSubtasks();
            return allEpicSubtasks;
        }
        return null;
    }

///////////////////////
    public Subtask createSubtask(Subtask newSubtask) {
        int newId = idGenerator();

        if (epics.containsKey(newSubtask.getEpicId())) {
            newSubtask.setId(newId);//
            Epic existingEpic = epics.get(newSubtask.getEpicId());
            existingEpic.addSubtask(newSubtask); //добавляем сабтаск в лист эпика
            existingEpic.updateStatusByTasks();
            return subtasks.put(newId, newSubtask);
        }
        return null;
    }

    public boolean updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask existingSubtask = subtasks.get(subtask.getId());
            if (existingSubtask.getEpicId().equals(subtask.getEpicId())) {
                Epic existingEpic = epics.get(existingSubtask.getEpicId());
                existingEpic.deleteSubtask(existingSubtask);
                existingEpic.addSubtask(subtask);
                subtasks.put(subtask.getId(), subtask);
                existingEpic.updateStatusByTasks();
            }
            return true;
        }
        return false;// проверять все подзадачи
    }

    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.deleteSubtask(subtask);
            epic.updateStatusByTasks();
        }
        }



    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());

    }

    public void deteteAllSubtusks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasks();
            epic.updateStatusByTasks();
        }
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);

    }

    private int idGenerator(){
        return idCounter++;
    }

}
