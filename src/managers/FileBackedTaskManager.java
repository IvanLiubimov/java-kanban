package managers;

import exceptions.FileManagerFileRecoveryException;
import exceptions.FileManagerFileSaveException;
import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File data;

    public FileBackedTaskManager(File data, InMemoryHistoryManager historyManager) {
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
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
    }

    private void save() {
           for (Task task : tasks.values()) {
               String taskAsString = task.toString();
               writeStringToFile(taskAsString);
           }
           for (Epic epic : epics.values()) {
               String epicAsString = epic.toString();
               writeStringToFile(epicAsString);
           }
           for (Subtask subtusk : subtasks.values()) {
               String subtuskAsString = subtusk.toString();
               writeStringToFile(subtuskAsString);
           }
       }


    private void writeStringToFile(String taskAsString) {
        try (FileWriter fileWriter = new FileWriter(data)) {
            fileWriter.write(taskAsString);
            fileWriter.write(System.lineSeparator());

        } catch (IOException e) {
            String error = "Не удаётся сохранить данные" + e.getMessage();
            System.out.println(error);
            throw new FileManagerFileSaveException(error);
        }
    }


    public static FileBackedTaskManager loadFromFile(File file) throws FileManagerFileRecoveryException {
        try {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file, new InMemoryHistoryManager());
            List<String> allFileContent = Files.readAllLines(file.toPath());
            for (String line : allFileContent) {
                 Task task = fileBackedTaskManager.fromString(line);
                 int maxId = 0;
                 if (maxId < task.getId()) {
                     maxId = task.getId();
                 }
                if (task instanceof Epic) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }
                fileBackedTaskManager.setIdCounter(maxId + 1);
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            String error = "Ошибка при восстановлении файла" + e.getMessage();
            System.out.println(error);
            throw new FileManagerFileRecoveryException(error);
        }
    }

    public Task fromString(String line) {
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
                epic.updateStatusByTasks();
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
        }
        return null;
    }
    }
