package task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private Instant endTime;


    @Override
    public String toString() {
        return super.toString();
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW, Duration.ZERO, null);
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

    public void updateDurationByTasks() {
            long totalDuration = subtasks.stream()
                    .mapToLong(subtask -> subtask.getDuration().toMillis())
                    .sum();
            Duration epicDuration = Duration.ofMillis(totalDuration);
            this.setDuration(epicDuration);
    }

    public void updateStartTimeByTasks() {
        List<Subtask> subtasksWithTime = subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .toList();
        boolean isEpicHasTime = !subtasksWithTime.isEmpty();
        if (isEpicHasTime) {
            long newStartTime = subtasksWithTime.stream()
                    .mapToLong(subtask -> subtask.getStartTime().toEpochMilli())
                    .min()
                    .getAsLong();
            Instant epicStartTime = Instant.ofEpochMilli(newStartTime);
            this.setStartTime(epicStartTime);
        } else {
            this.setStartTime(null);
        }
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void calculateEndTime() {
        List<Subtask> subtasksWithTime = subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .toList();
        boolean isEpicHasTime = !subtasksWithTime.isEmpty();
        if (isEpicHasTime) {
            long newEndTime = subtasksWithTime.stream()
                    .mapToLong(subtask -> subtask.getEndTime().toEpochMilli())
                    .max()
                    .getAsLong();
            this.endTime = Instant.ofEpochMilli(newEndTime);
        } else {
            endTime = null;
        }

    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public String getType() {
        return "EPIC";
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
