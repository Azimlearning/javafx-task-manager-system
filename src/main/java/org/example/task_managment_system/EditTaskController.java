package org.example.task_managment_system;

import Connection.DBConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditTaskController {
    private Connection connection;

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
        connection = DBConnect.getConnect();
        String updatedName = taskNameInput.getText();
        String updatedDescription = taskDescriptionInput.getText().trim().isEmpty() ? null : taskDescriptionInput.getText();
        LocalDate updatedStartDate = startDatePicker.getValue();
        LocalDate updatedEndDate = endDatePicker.getValue();

        // Update the task object with edited values
        taskToEdit.setName(updatedName);
        taskToEdit.setDescription(updatedDescription);
        taskToEdit.setStartDate(updatedStartDate);
        taskToEdit.setEndDate(updatedEndDate);

        taskToEdit.setName(updatedName);
        taskToEdit.setDescription(updatedDescription);
        taskToEdit.setStartDate(updatedStartDate);
        taskToEdit.setEndDate(updatedEndDate);

        // Prepare and execute SQL statement
        String sql = "UPDATE Tasklist SET TaskName = ?, StartDate = ?, EndDate = ?, TaskDescription = ? WHERE TaskID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, updatedName);
            preparedStatement.setString(2, String.valueOf(updatedStartDate));
            preparedStatement.setString(3, String.valueOf(updatedEndDate));
            preparedStatement.setString(4, updatedDescription);
            preparedStatement.setInt(5, taskToEdit.getID()); // Assuming 'id' is the primary key

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task updated successfully!");
                // (Optional) Update your local task list with the edited task
                // ...

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to edit");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }

        // (Optional) Persist changes to task data file (if applicable)

        // Close the modal window
        ((Stage) saveButton.getScene().getWindow()).close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void closeWindow(ActionEvent event) {
        // Close the modal window without saving changes
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
