package org.example.task_managment_system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import Connection.DBConnect;

import javafx.application.Application;
import javafx.application.Platform;
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

    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;



    private ObservableList<Task> tasks;
    String query = null;
    Connection connection;
    private Task task;


    public TaskMakerController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://techcaredb.cbg6264eke46.ap-southeast-2.rds.amazonaws.com :3306/TaskManagerSytem", "admin", "shukri1234");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    public void initialize() throws SQLException {
        timeNow();
        tasks = FXCollections.observableArrayList();
        ListView<Task> listView = new ListView<>(tasks);
        ObservableList<Task> tasks = refreshList();
        eventList.setItems(tasks);

        // Execute the query and process results
        refreshList();

        // Read tasks from file on startup
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


    public void enterTask(ActionEvent event) throws IOException, SQLException {
        String name = taskInput.getText();
        String description = taskDiscription.getText().trim().isEmpty() ? null : taskDiscription.getText();
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();
        String StatusTask;
        String sql = "SELECT * FROM Tasklist";



        if (name.isEmpty() || startDateValue == null || endDateValue == null) {
            // Handle empty fields: display error message or alert user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Task");
            alert.setHeaderText("Please fill in all fields");
            alert.setContentText("Name, Start Date, and End Date are required for each task.");
            alert.showAndWait();
            return;
        }else{

            if(LocalDate.now().isAfter(endDate.getValue())){
                StatusTask= "Missed";
            }else{
                StatusTask= "Ongoing";
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Tasklist (TaskName, StartDate, EndDate, TaskDescription, TaskStatus) VALUES (?,?,?,?,?)")) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, String.valueOf(startDateValue));
                preparedStatement.setString(3, String.valueOf(endDateValue));
                preparedStatement.setString(4, description);
                preparedStatement.setString(5, StatusTask);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save SUCCESS");
                    alert.setHeaderText("Task saved successfully!");
                    alert.showAndWait();
                    clearAll();
                    System.out.println("Order submitted successfully!");
                } else {
                    System.out.println("Failed to submit order. Please try again.");
                }


                // Clear input fields after adding task
                taskInput.clear();
                taskDiscription.clear();
                startDate.setValue(null);
                endDate.setValue(null);

                // Update event list with new task
                eventList.refresh();
                eventList.setItems(refreshList());
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        /*Task newTask = new Task(name, description, startDateValue, endDateValue, StatusTask);
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
        }*/
    }

    private ObservableList<Task> refreshList() throws SQLException{
        ObservableList<Task> tasks = FXCollections.observableArrayList();

        connection= DBConnect.getConnect();
        String sql = "SELECT * FROM Tasklist";
        try{
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            int id = results.getInt("TaskID");
            String name = results.getString("TaskName");
            LocalDate startdate = results.getDate("StartDate").toLocalDate();
            LocalDate enddate = results.getDate("EndDate").toLocalDate();
            String taskdesc = results.getString("TaskDescription");
            String taskstatus = results.getString("TaskStatus");


            // Create a Product object and add it to the list
            tasks.add(new Task(id, name, taskdesc, startdate, enddate, taskstatus));
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tasks;

    }

    private void clearAll() throws SQLException {
        taskInput.clear();
        taskDiscription.clear();
        startDate.setValue(null);
        endDate.setValue(null);

        // Update event list with new task
        eventList.setItems(refreshList());
    }

    public void deleteTask(ActionEvent event) throws SQLException {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            DeletefromDB(selectedTask.getID());
            eventList.setItems(refreshList());
            //updateTaskCounts();

        } else {
            // Display error message within the GUI
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task to delete.");
            alert.showAndWait();
        }
    }


    private void DeletefromDB(int taskId) {

        String sql = "DELETE FROM Tasklist WHERE TaskID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }

    public void markTaskComplete(ActionEvent event) throws SQLException {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            markComplete(selectedTask.getID());
            eventList.setItems(refreshList());
        } else {
            // Display error message using an Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to mark complete.");
            alert.showAndWait();
        }
    }

    private void markComplete(int taskId){
        String sql = "UPDATE `Tasklist` SET `TaskStatus`= ? WHERE TaskID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "Completed");
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void uncompleteTask(ActionEvent event) throws SQLException {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            markIncomplete(selectedTask.getID()); // Mark the task as uncompleted
            eventList.setItems(refreshList());
        } else {
            // Display error message using an Alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Task Selected");
            alert.setHeaderText("Please select a task to mark uncomplete.");
            alert.showAndWait();
        }
    }//Please select a task to mark uncomplete.

    private void markIncomplete(int taskId){
        String sql = "UPDATE `Tasklist` SET `TaskStatus`= ? WHERE TaskID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "Ongoing");
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


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

    public void timeNow() { //clock function (not tested yet)
        Thread thread = new Thread(() -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, EEEE"); //format
            try {
                while (!Thread.currentThread().isInterrupted()) { //infinite loop
                    Thread.sleep(1000);
                    final String timeNow = timeFormat.format(new java.util.Date());
                    final String dateNow = dateFormat.format(new Date());
                    Platform.runLater(() -> {
                        currentTime.setText(timeNow);
                        currentDate.setText(dateNow); //labelname
                    });
                }
            } catch (InterruptedException e) {
                //do nothing
            }
        });
        thread.start();
    }


}
