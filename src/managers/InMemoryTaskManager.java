package managers;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Epic> epics;
    protected Map<Integer, Subtask> subtasks;
    protected int idCounter = 0;
    private HistoryManager historyManager;
    private Comparator<Task> taskComparator = (o1, o2) -> {
        long t1 = o1.getStartTime().toEpochMilli();
        long t2 = o2.getStartTime().toEpochMilli();
        return (int) (t1 - t2);
    };
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

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

        boolean hasInteractions = isTaskTimeInConflict(newTask);
        if (hasInteractions) {
            throw new IllegalArgumentException("Нельзя создать задачу: время выполнения пересекается с другой задачей");
        } else {
            int newId = idGenerator();

            newTask.setId(newId);
            tasks.put(newTask.getId(), newTask);
            return newTask;
        }

    }

    @Override
    public boolean updateTask(Task task) {

        if (tasks.containsKey(task.getId())) {
            boolean hasInteractions = isTaskTimeInConflict(task);
            if (hasInteractions) {
                throw new IllegalArgumentException("Нельзя обновить задачу: время выполнения обновлённой задачи пересекается с другой задачей");
            }
            Task existingTask = tasks.get(task.getId());
            existingTask.setName(task.getName());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return true;
        } else {
            return false;
        }
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
        Task seenTask = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getDuration(), task.getStartTime());
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
    boolean hasInteractions = isTaskTimeInConflict(newSubtask);
    if (hasInteractions) {
        throw new IllegalArgumentException("Нельзя создать задачу: время выполнения пересекается с другой задачей");
    } else {
        if (epics.containsKey(newSubtask.getEpicId())) {
            newSubtask.setId(newId);//
            Epic existingEpic = epics.get(newSubtask.getEpicId());
            existingEpic.addSubtask(newSubtask); //добавляем сабтаск в лист эпика
            existingEpic.updateStatusByTasks();
            existingEpic.updateStartTimeByTasks();
            existingEpic.updateDurationByTasks();
            subtasks.put(newSubtask.getId(), newSubtask);
            return newSubtask;
        }
        return null;
    }
}

    @Override
    public boolean updateSubtask(Subtask subtask) {

        if (subtasks.containsKey(subtask.getId())) {
            boolean hasInteractions = isTaskTimeInConflict(subtask);
            if (hasInteractions) {
                throw new IllegalArgumentException("Нельзя обновить задачу: время выполнения обновлённой задачи пересекается с другой задачей");
            }
            Subtask existingSubtask = subtasks.get(subtask.getId());
            if (existingSubtask.getEpicId().equals(subtask.getEpicId())) {
                Epic existingEpic = epics.get(existingSubtask.getEpicId());
                existingEpic.deleteSubtask(existingSubtask);
                existingEpic.addSubtask(subtask);
                subtasks.put(subtask.getId(), subtask);
                existingEpic.updateStatusByTasks();
                existingEpic.updateStartTimeByTasks();
                existingEpic.updateDurationByTasks();
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
            epic.updateStartTimeByTasks();
            epic.updateDurationByTasks();
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
        Subtask seenSubtask = new Subtask(subtask.getName(), subtask.getDescription(),subtask.getStatus(),
                subtask.getDuration(), subtask.getStartTime(),subtask.getEpicId());
        historyManager.addToSeenTasks(seenSubtask);
        return subtasks.get(id);
    }

    private int idGenerator() {
        return idCounter = idCounter + 1;
    }

    public boolean isIdConflict(int id) {
        return tasks.containsKey(id);
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
    prioritizedTasks.clear();
    try {
        prioritizedTasks.addAll(tasks.values().stream()
                .filter(tasksWithStartTime -> tasksWithStartTime.getStartTime() != null)
                .filter(tasksWithDuration -> tasksWithDuration.getDuration() != null)
                .toList());
        prioritizedTasks.addAll(subtasks.values().stream()
                .filter(tasksWithStartTime -> tasksWithStartTime.getStartTime() != null)
                .filter(tasksWithDuration -> tasksWithDuration.getDuration() != null)
                .toList());
        return prioritizedTasks;
    } catch (NullPointerException e) {
        System.out.println("У одного или нескольких эпиков нет подзадач. Добавьте подзадачи и их начальное и продолжительность");
        return Collections.emptySet();
    }
    }

    private boolean isTimeInConflict(Task t1, Task t2) {
        long startT1 = t1.getStartTime().toEpochMilli();
        long startT2 = t2.getStartTime().toEpochMilli();
        long endT1 = t1.getEndTime().toEpochMilli();
        long endT2 = t2.getEndTime().toEpochMilli();
        if ((startT1 <= startT2 && endT1 <= startT2) || (startT1 >= endT2 && endT1 >= endT2)) {
            return false;
        }
        return true;
    }

    private boolean isTaskTimeInConflict(Task newTask) {
    ArrayList<Task> allTasks = new ArrayList<>();
    allTasks.addAll(tasks.values());
    allTasks.addAll(subtasks.values());
    if (allTasks.stream()
            .filter(existingTask -> existingTask.getId() != newTask.getId())
            .anyMatch(task -> isTimeInConflict(task, newTask))) {
        return true;
    }
        return false;
    }
}

