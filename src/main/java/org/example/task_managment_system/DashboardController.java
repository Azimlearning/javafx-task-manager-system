package org.example.task_managment_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.DBConnect;

public class DashboardController { // Implement Initializable

    private Connection connection = null;
    String query = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private TableColumn<Task, String> nameCol;

    @FXML
    private TableColumn<Task, String> endCol;
    @FXML
    private TableColumn<Task, String> statusCol;

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

    @FXML
    private PieChart pieChart;

    ObservableList<Task> TaskList = FXCollections.observableArrayList();

    //@Override
    public void initialize() throws SQLException {//URL location, ResourceBundle resources

        connection = DBConnect.getConnect(); // Assuming DBConnect provides a connection

        // Set cell value factories once
        //idCol.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("StatusTask"));

        refreshList();

        taskTableView.setItems(TaskList); // Set items only once after population

        totalTasksLabel.setText(String.valueOf(totaltask));
        completedTasksLabel.setText(String.valueOf(completedTask));
        missedTasksLabel.setText(String.valueOf(missedTask));
        ongoingTasksLabel.setText(String.valueOf(onGoingTask));

        loadChart();
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

                String currentstatus = resultSet.getString("TaskStatus");
                if (currentstatus.equals("Completed")) { // Use equals() for string comparison
                    completedTask++;
                } else if (currentstatus.equals("Missed")) {
                    missedTask++;
                } else {
                    onGoingTask++;
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(TaskDisplayController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void loadChart() {

        pieChart.getData().clear(); // Clear existing chart data

        PieChart.Data slice1 = new PieChart.Data("Missed (" + missedTask + ")", missedTask);
        PieChart.Data slice2 = new PieChart.Data("OnGoing (" + onGoingTask + ")", onGoingTask);
        PieChart.Data slice3 = new PieChart.Data("Completed (" + completedTask + ")", completedTask);

        pieChart.getData().addAll(slice1, slice2, slice3); // Efficiently add all slices
    }
}
