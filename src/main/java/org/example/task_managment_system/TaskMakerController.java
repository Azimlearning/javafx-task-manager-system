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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Task");
            alert.setHeaderText("Please fill in all fields");
            alert.setContentText("Name, Start Date, and End Date are required for each task.");
            alert.showAndWait();
            return;
        }

        Task newTask = new Task(name, description, startDateValue, endDateValue);
        tasks.add(newTask);

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

    public void deleteTask(ActionEvent event) {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            eventList.refresh();
            //updateTaskCounts();
            saveTasksToFile();
        } else {
            // Display error message within the GUI
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task to delete.");
            alert.showAndWait();
        }
    }


    private void saveTasksToFile() {
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

    public void markTaskComplete(ActionEvent event) {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(true); // Mark the task as completed
            eventList.refresh(); // Update the list view
        } else {
            // Display error message using an Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to mark complete.");
            alert.showAndWait();
        }
    }

    public void uncompleteTask(ActionEvent event) {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(false); // Mark the task as uncompleted
            eventList.refresh(); // Update the list view
        } else {
            // Display error message using an Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to mark uncomplete.");
            alert.showAndWait();
        }
    }//Please select a task to mark uncomplete.


    public void editTask(ActionEvent event) {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edittask.fxml"));
                Parent root = loader.load();
                EditTaskController editController = loader.getController();
                editController.setTaskToEdit(selectedTask);

                Stage editStage = new Stage();
                editStage.setTitle("Edit Task");
                editStage.setScene(new Scene(root));
                editStage.initModality(Modality.APPLICATION_MODAL);
                editStage.showAndWait();

                eventList.refresh(); // Update list view after potential changes
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to edit.");
            alert.showAndWait();
        }
    }


}
