public class Subtask extends Task{
    private Integer epicId;

    public Integer getEpicId() {
        return epicId;
    }
    

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
}
