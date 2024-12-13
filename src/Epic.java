import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtasks = subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public Status getStatusByTasks(Epic epic) {
        for (Subtask subtask : subtasks) {
            Status subtaskStatus = subtask.getStatus();
            if (subtaskStatus == Status.NEW || subtaskStatus == Status.IN_PROGRESS){
                return Status.IN_PROGRESS;
            } else {
                return Status.DONE;
            }
        }
        return epic.getStatus();// статус текущего эпика в зависимости от статуса списка подзадач

    }

    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

}
