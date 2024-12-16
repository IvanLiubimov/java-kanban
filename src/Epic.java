import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        setStatus(Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>();
    }

    public Status getStatusByTasks(Epic epic) {
        Status epicStatus = epic.getStatus();
        if (subtasks.isEmpty()) {
            epicStatus = Status.NEW;
            return epicStatus;
        }
        boolean allSubtasksDone = true;
        boolean anyOfSubtasksInProgress = false;
        for (Subtask subtask : subtasks) {
            Status subtaskStatus = subtask.getStatus();
            if (subtaskStatus != Status.DONE) {
                allSubtasksDone = false;
            } else if (subtaskStatus == Status.IN_PROGRESS) {
                anyOfSubtasksInProgress = true;
            }
        }
        if (allSubtasksDone) {
            epicStatus = Status.DONE;
        } else if (anyOfSubtasksInProgress) {
            epicStatus = Status.IN_PROGRESS;
        } else {
            epicStatus = Status.NEW;
        }
        return epicStatus;
        }
       // статус текущего эпика в зависимости от статуса списка подзадач



    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public void deleteSubtask (Subtask subtask){
        subtasks.remove(subtask);
    }

    public void deleteAllSubtasks (){
        subtasks.clear();
    }

}
