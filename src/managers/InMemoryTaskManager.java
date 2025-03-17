package managers;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Epic> epics;
    protected Map<Integer, Subtask> subtasks;
    protected int idCounter = 0;
    private HistoryManager historyManager;



    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyManager.getSeenTasks());
    }

    protected void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public int getIdCounter() {
        return idCounter;
    }

    @Override
    public Task createTask(Task newTask) {
        int newId = idGenerator();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;

    }

    @Override
    public boolean updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task existingTask = tasks.get(task.getId());
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return true;
        }
        return false;

    }

    @Override
    public Task deleteTaskById(Integer id) {
        historyManager.removeNode(id);
        return tasks.remove(id);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());

    }

    @Override
    public Task findTaskById(Integer id) {
        Task task = tasks.get(id);
        Task seenTask = new Task(task.getName(), task.getDescription(), task.getStatus());
        historyManager.addToSeenTasks(seenTask);
        return tasks.get(id);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.removeNode(taskId);
        }
        tasks.clear();
    }
//////////////////////////

@Override
public Epic createEpic(Epic newEpic) {
        int newId = idGenerator();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;

    }

    @Override
    public Epic deleteEpicById(Integer id) { // 1) есть ли айди 2) залезай в талицу сабтасков и удаляй сабтаски
        if (epics.containsKey(id)) {
            Epic existingEpic = epics.get(id);
            ArrayList<Subtask> epicSubtasks = existingEpic.getSubtasks();
            for (Subtask subtask : epicSubtasks) {
                Integer idToDelete = subtask.getId();
                historyManager.removeNode(idToDelete);
                subtasks.remove(idToDelete);
                }
            historyManager.removeNode(id);
            return epics.remove(id);
        }
        return null;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            return true;
        }
        return false;// проверять все подзадачи
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());


    }

    @Override
    public Epic findEpicById(Integer id) {
        Epic epic = epics.get(id);
        Task seenEpic = new Epic(epic.getName(), epic.getDescription());
        seenEpic.setId(epic.getId());
        seenEpic.setStatus(epic.getStatus());
        historyManager.addToSeenTasks(seenEpic);
        return epics.get(id);
    }

    @Override
    public void deleteAllEpics() {
        for (Integer subtaskId : subtasks.keySet()) {
            historyManager.removeNode(subtaskId);
        }
        for (Integer epicId : epics.keySet()) {
            historyManager.removeNode(epicId);
        }


        subtasks.clear();
        epics.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Subtask> allEpicSubtasks = epic.getSubtasks();
            return allEpicSubtasks;
        }
        return null;
    }

///////////////////////
@Override
public Subtask createSubtask(Subtask newSubtask) {
        int newId = idGenerator();

        if (epics.containsKey(newSubtask.getEpicId())) {
            newSubtask.setId(newId);//
            Epic existingEpic = epics.get(newSubtask.getEpicId());
            existingEpic.addSubtask(newSubtask); //добавляем сабтаск в лист эпика
            existingEpic.updateStatusByTasks();
            subtasks.put(newSubtask.getId(), newSubtask);
            return newSubtask;
        }
        return null;
    }

    @Override
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

    @Override
    public void deleteSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.remove(id);
            historyManager.removeNode(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.deleteSubtask(subtask);
            epic.updateStatusByTasks();
        }
        }



    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());

    }

    @Override
    public void deteteAllSubtusks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : subtasks.keySet()) {
                historyManager.removeNode(subtaskId);
            }
            epic.deleteAllSubtasks();
            epic.updateStatusByTasks();
        }
    }

    @Override
    public Subtask findSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        Subtask seenSubtask = new Subtask(subtask.getName(), subtask.getDescription(),subtask.getStatus(), subtask.getEpicId());
        historyManager.addToSeenTasks(seenSubtask);
        return subtasks.get(id);
    }

    private int idGenerator() {
        return idCounter = idCounter + 1;
    }

    public boolean isIdConflict(int id) {
        return tasks.containsKey(id);
    }

}
