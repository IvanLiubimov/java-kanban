package task;

import exceptions.EpicNoSubtasksException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;


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
        try {
            long totalDuration = subtasks.stream()
                    .mapToLong(subtask -> subtask.getDuration().toMillis())
                    .sum();
            Duration epicDuration = Duration.ofMillis(totalDuration);
            this.setDuration(epicDuration);
        } catch (EpicNoSubtasksException e) {
            String error = "У эпика еще нет подзадач" + e.getMessage();
            System.out.println(error);
            throw new EpicNoSubtasksException(error);
        }
    }

    public void updateStartTimeByTasks() {
            long newStartTime = subtasks.stream()
                    .mapToLong(subtask -> subtask.getStartTime().toEpochMilli())
                    .min()
                    .orElseThrow(() -> new EpicNoSubtasksException("У эпика еще нет подзадач"));
            Instant epicStartTime = Instant.ofEpochMilli(newStartTime);
            this.setStartTime(epicStartTime);
    }

    @Override
    public Instant getEndTime() {
        long newEndTime = subtasks.stream()
                .mapToLong(subtask -> subtask.getEndTime().toEpochMilli())
                .max()
                .orElseThrow(() -> new EpicNoSubtasksException("У эпика еще нет подзадач"));
        return Instant.ofEpochMilli(newEndTime);
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
