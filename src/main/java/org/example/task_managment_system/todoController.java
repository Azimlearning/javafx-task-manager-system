package org.example.task_managment_system;

import javafx.application.Platform;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.DBConnect;

public class todoController {

    private Connection connection = null;
    String query = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private TableColumn<Task, String> nameCol;

    @FXML
    private TableColumn<Task, String> descCol;

    @FXML
    private Label currentTime;

    @FXML
    private Label currentDate;


    ObservableList<Task> TaskList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        timeNow();
        connection = DBConnect.getConnect();
        // Set cell value factories once
        //idCol.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
        //nameCol.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
        //startCol.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        //endCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        //statusCol.setCellValueFactory(new PropertyValueFactory<>("StatusTask"));
        //descCol.setCellValueFactory(new PropertyValueFactory<>("TaskDescription"));

        refreshList();
        taskTableView.setItems(TaskList); // Set items only once after population

    }

    private void refreshList() throws SQLException {
        TaskList.clear(); // Clear existing data before refreshing

        try {
            TaskList.clear();

            query = "SELECT * FROM `Tasklist`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
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

    public void timeNow() { //clock function (not tested yet)
        Thread thread = new Thread(() -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, EEEE"); //format
            try {
                while (!Thread.currentThread().isInterrupted()) { //infinite loop
                    Thread.sleep(1000);
                    final String timeNow = timeFormat.format(new Date());
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
