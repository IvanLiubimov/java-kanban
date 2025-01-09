package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void updateStatusByTasks() {
        Status epicStatus;
        if (subtasks.isEmpty()) {
            epicStatus = Status.NEW;
            this.setStatus(epicStatus);
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
       this.setStatus(epicStatus);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return Objects.equals(subtasks, epic.getSubtasks());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (subtasks != null) {
            hash = hash + subtasks.hashCode();
        }
        hash = hash * 31;

        if (subtasks != null) {
            hash = hash + subtasks.hashCode();
        }
        return hash;
    }
}
