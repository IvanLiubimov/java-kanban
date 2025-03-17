package managers;

import exceptions.FileManagerRecoveryException;
import exceptions.FileManagerSaveException;
import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File data;

    public FileBackedTaskManager(File data) {
        this.data = data;
    }

    @Override
    public Task createTask(Task newTask) {
        Task createdTask = super.createTask(newTask);
        save();
        return createdTask;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean updatedTask = super.updateTask(task);
        if (updatedTask) {
            save();
        }
         return updatedTask;
    }

    @Override
    public Task deleteTaskById(Integer id) {
        Task deletedTask = super.deleteTaskById(id);
        if (deletedTask != null) {
            save();
        }
        return deletedTask;
    }

    @Override
    public Epic deleteEpicById(Integer id) {
        Epic deletedEpic = super.deleteEpicById(id);
        if (deletedEpic != null) {
            save();
        }
        return deletedEpic;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean updatedEpic = super.updateEpic(epic);
        if (updatedEpic) {
            save();
        }
        return updatedEpic;

    }

    @Override
    public Epic createEpic(Epic newEpic) {
        Epic createdEpic = super.createEpic(newEpic);
        save();
        return createdEpic;
    }

    @Override
    public Subtask createSubtask(Subtask newSubtask) {
        Subtask createdSubtask = super.createSubtask(newSubtask);
        save();
        return createdSubtask;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean updatedSubtask = super.updateSubtask(subtask);
        if (updatedSubtask) {
            save();
        }
        return updatedSubtask;
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deteteAllSubtusks() {
        super.deteteAllSubtusks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(data)) {
            fileWriter.write("id,type,name,status,description,epicId");
            fileWriter.write(System.lineSeparator());

           for (Task task : tasks.values()) {
               fileWriter.write(task.toString());
               fileWriter.write(System.lineSeparator());
           }
           for (Epic epic : epics.values()) {
               fileWriter.write(epic.toString());
               fileWriter.write(System.lineSeparator());
           }
           for (Subtask subtusk : subtasks.values()) {
               fileWriter.write(subtusk.toString());
               fileWriter.write(System.lineSeparator());
           }
        } catch (IOException e) {
            String error = "Не удаётся сохранить данные" + e.getMessage();
            System.out.println(error);
            throw new FileManagerSaveException(error);
        }
       }


    public static FileBackedTaskManager loadFromFile(File file) throws FileManagerRecoveryException {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            List<String> allFileContent = Files.readAllLines(file.toPath());
            int maxId = 0;
            for (int i = 1; i < allFileContent.size(); i++) {
                String line = allFileContent.get(i);
                 Task task = fileBackedTaskManager.fromString(line);
                 if (maxId < task.getId()) {
                     maxId = task.getId();
                 }
                if (task instanceof Epic) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                    int epicId = ((Subtask) task).getEpicId();
                    Epic epicToAddSubtask = fileBackedTaskManager.epics.get(epicId);
                    epicToAddSubtask.addSubtask((Subtask) task);
                    epicToAddSubtask.updateStatusByTasks();
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }
            }
            fileBackedTaskManager.setIdCounter(maxId + 1);
            return fileBackedTaskManager;
        } catch (IOException e) {
            String error = "Ошибка при восстановлении файла" + e.getMessage();
            System.out.println(error);
            throw new FileManagerRecoveryException(error);
        }
    }

    protected Task fromString(String line) {
        String[] partsOfLine = line.split(",");

        int id = Integer.parseInt(partsOfLine[0]);
        TaskTypes type = TaskTypes.valueOf(partsOfLine[1]);
        String name = partsOfLine[2];
        String description = partsOfLine[3];
        Status status = Status.valueOf(partsOfLine[4]);
        int epicId = Integer.parseInt(partsOfLine[5]);
        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
        }
        return null;
    }
    }
