package task;
import java.time.*;
import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private Instant startTime;

    public Task(String name, String description, Status status, Duration duration, Instant startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getType() {
        return "TASK";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        // Сравниваем только по id
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + "," + getType() + "," + name + "," + description + "," + status + "," + duration + "," + startTime;
    }
}
