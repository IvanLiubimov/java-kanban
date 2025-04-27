package task;

import java.time.Duration;
import java.time.Instant;

public class Subtask extends Task {
    private Integer epicId;

    public Integer getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, Status status, Duration duration, Instant startTime, Integer epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String getType() {
        return "SUBTASK";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return epicId.equals(subtask.epicId);
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(epicId);
    }
}
