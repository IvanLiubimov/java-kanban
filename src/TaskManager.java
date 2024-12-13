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
    public Task updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            Task existingTask = tasks.get(task.getId());
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return tasks.get(existingTask.getId());
        }
        return null;

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
            existingEpic.getSubtasks().clear();
            return epics.remove(id);
        }
        return null;
    }

    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existingEpic = epics.get(epic.getId());
            existingEpic.setName(epic.getName());
            existingEpic.setDescription(epic.getDescription());
            existingEpic.setStatus(epic.getStatusByTasks(epic));
            return epics.get(existingEpic.getId());
        }
        return null;// проверять все подзадачи
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());


    }

    public Epic findEpicById(Integer id) {
        return epics.get(id);
    }
///////////////////////
    public Subtask createSubtask(Subtask newSubtask) {
        int newId = idGenerator();
        newSubtask.setId(newId);
        if (epics.containsKey(newSubtask.getEpicId())) { //
            Epic existingEpic = epics.get(newSubtask.getEpicId());
            existingEpic.addSubtask(newSubtask); //добавляем сабтаск в лист эпика
            return subtasks.put(newId, newSubtask);
        }
        return null;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask existingSubtask = subtasks.get(subtask.getId());
            if (epics.containsKey(existingSubtask.getEpicId())) {
                Epic existingEpic = epics.get(existingSubtask.getEpicId());
                ArrayList<Subtask> subtasksOfEpic = existingEpic.getSubtasks();
                subtasksOfEpic.remove(existingSubtask);
                createTask(subtask);
                existingSubtask.setEpicId(subtask.getEpicId());
                existingSubtask.setName(subtask.getName());
                existingSubtask.setDescription(subtask.getDescription());
                existingSubtask.setStatus(subtask.getStatus());
            }
            return subtasks.get(existingSubtask.getId());
        }
        return null;// проверять все подзадачи
    }

    public Subtask deleteSubtaskById(Integer id) {
        return subtasks.remove(id);
        }



    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());

    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);

    }

    private int idGenerator(){
        return idCounter++;
    }

}
