package org.example.task_managment_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditTaskController {

    @FXML
    private TextField taskNameInput;

    @FXML
    private TextArea taskDescriptionInput;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Task taskToEdit;  // Task object to be edited

    public void setTaskToEdit(Task task) {
        this.taskToEdit = task;

        // Pre-populate input fields with task details
        taskNameInput.setText(task.getName());
        taskDescriptionInput.setText(task.getDescription() == null ? "" : task.getDescription());
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());
    }

    @FXML
    public void saveTask(ActionEvent event) {
        String updatedName = taskNameInput.getText();
        String updatedDescription = taskDescriptionInput.getText().trim().isEmpty() ? null : taskDescriptionInput.getText();
        LocalDate updatedStartDate = startDatePicker.getValue();
        LocalDate updatedEndDate = endDatePicker.getValue();

        // Update the task object with edited values
        taskToEdit.setName(updatedName);
        taskToEdit.setDescription(updatedDescription);
        taskToEdit.setStartDate(updatedStartDate);
        taskToEdit.setEndDate(updatedEndDate);

        // (Optional) Persist changes to task data file (if applicable)

        // Close the modal window
        ((Stage) saveButton.getScene().getWindow()).close();
    }

    @FXML
    public void closeWindow(ActionEvent event) {
        // Close the modal window without saving changes
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
