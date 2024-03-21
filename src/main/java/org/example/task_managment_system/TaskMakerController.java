package org.example.task_managment_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

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
        eventList.setItems(tasks);
    }

    public void enterTask(ActionEvent event) {
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

        // Clear input fields after adding task
        taskInput.clear();
        taskDiscription.clear();
        startDate.setValue(null);
        endDate.setValue(null);

        // Update event list with new task
        eventList.refresh();
    }
}
