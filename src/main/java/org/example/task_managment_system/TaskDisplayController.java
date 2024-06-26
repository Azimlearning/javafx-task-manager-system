package org.example.task_managment_system;

import Connection.DBConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDisplayController {
    private Connection connection = null;
    String query = null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> idCol;
    @FXML
    private TableColumn<Task, String> nameCol;
    @FXML
    private TableColumn<Task, String> startCol;
    @FXML
    private TableColumn<Task, String> endCol;
    @FXML
    private TableColumn<Task, String> statusCol;
    @FXML
    private TableColumn<Task, String> descCol;
    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label completedTasksLabel;
    @FXML
    private Label missedTasksLabel;
    @FXML
    private Label ongoingTasksLabel;

    private int totaltask = 0;
    private int completedTask = 0;
    private int missedTask = 0;
    private int onGoingTask = 0;


    ObservableList<Task> TaskList = FXCollections.observableArrayList(); // List to store data from database


        // ...

        public void initialize() throws SQLException {
            connection = DBConnect.getConnect();
            // Set cell value factories once
            idCol.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
             //nameCol.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
            //startCol.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
            //endCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("StatusTask"));
            //descCol.setCellValueFactory(new PropertyValueFactory<>("TaskDescription"));
            // ... set other cell value factories

            refreshList();
            taskTableView.setItems(TaskList); // Set items only once after population

            totalTasksLabel.setText(String.valueOf(totaltask));
            completedTasksLabel.setText(String.valueOf(completedTask));
            missedTasksLabel.setText(String.valueOf(missedTask));
            ongoingTasksLabel.setText(String.valueOf(onGoingTask));

        }

        private void refreshList() throws SQLException {
            TaskList.clear(); // Clear existing data before refreshing

            try {
                TaskList.clear();

                query = "SELECT * FROM `Tasklist`";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    totaltask++;
                    TaskList.add(new Task(
                            resultSet.getInt("TaskID"),
                            resultSet.getString("TaskName"),
                            resultSet.getString("TaskDescription"),
                            resultSet.getDate("StartDate").toLocalDate(),
                            resultSet.getDate("EndDate").toLocalDate(),
                            resultSet.getString("TaskStatus")));
                    taskTableView.setItems(TaskList);
                    String currentstatus = resultSet.getString("TaskStatus");
                    //System.out.println(currentstatus); //check currentstatus catches TaskStatus values
                    if (currentstatus.equals("Completed")) { // Use equals() for string comparison
                        completedTask++;
                    } else if (currentstatus.equals("Missed")) {
                        missedTask++;
                    } else {
                        onGoingTask++;
                    }






                        /*int id = results.getInt("TaskID");
                        String name = results.getString("TaskName");
                        LocalDate startdate = results.getDate("StartDate").toLocalDate();
                        LocalDate enddate = results.getDate("EndDate").toLocalDate();
                        String taskdesc = results.getString("TaskDescription");
                        String taskstatus = results.getString("TaskStatus");

                        // Create a Task object and add it to the list
                        TaskList.add(new Task(id, name, taskdesc, startdate, enddate, taskstatus));*/
                    }

            } catch (SQLException e) {
                Logger.getLogger(TaskDisplayController.class.getName()).log(Level.SEVERE, null, e);
            }
        }


}


