package org.example.task_managment_system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TaskMakerController {

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField taskInput;

    @FXML
    private TextArea taskDiscription;

    @FXML
    private Label taskDescriptionLabel; // Renamed for consistency

    @FXML
    private ListView<Task> eventList;

    private ObservableList<Task> tasks;

    public void initialize() {
        tasks = FXCollections.observableArrayList();

        // Read tasks from file on startup (if it exists)
        try {
            String userHome = System.getProperty("user.home");
            File tasksFile = new File(userHome, "task-manager/tasks.dat");
            if (tasksFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(tasksFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                tasks = FXCollections.observableArrayList((ArrayList<Task>) objectInputStream.readObject());
                objectInputStream.close();
                fileInputStream.close();
                // Update task counts based on loaded tasks
                for (Task task : tasks) {
                    Task.updateTaskCounts(task);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading tasks from file: " + e.getMessage());
        }

        eventList.setItems(tasks);
    }

    public void enterTask(ActionEvent event) throws IOException {
        String name = taskInput.getText();
        String description = taskDiscription.getText().trim().isEmpty() ? null : taskDiscription.getText();
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();

        if (name.isEmpty() || startDateValue == null || endDateValue == null) {
            // Handle empty fields: display error message or alert user
            System.out.println("Please fill in all fields.");
            return;
        }

        Task newTask = new Task(name, description, startDateValue, endDateValue);
        tasks.add(newTask);
        Task.updateTaskCounts(newTask);  // Update task counts for the new task

        // Clear input fields after adding task
        taskInput.clear();
        taskDiscription.clear();
        startDate.setValue(null);
        endDate.setValue(null);

        // Update event list with new task
        eventList.refresh();

        // Write tasks to file
        try {
            String userHome = System.getProperty("user.home");
            File tasksFile = new File(userHome, "task-manager/tasks.dat");
            tasksFile.getParentFile().mkdirs(); // Create directories if they don't exist
            FileOutputStream fileOutputStream = new FileOutputStream(tasksFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            ArrayList<Task> serializableTasks = new ArrayList<>(tasks);
            objectOutputStream.writeObject(serializableTasks);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error writing tasks to file: " + e.getMessage());
        }
    }
}