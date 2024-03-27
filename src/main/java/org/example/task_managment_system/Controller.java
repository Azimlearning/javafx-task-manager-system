package org.example.task_managment_system;

import Connection.DBConnect;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Stylesheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller{ //implements Initializable

    private Connection connection = null;
    String query = null;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    @FXML
    public Button btnDashboard;

    @FXML
    public Button btnStudents;


    @FXML
    public Button btnTodo;

    @FXML
    public Button btnExit;

    @FXML
    public Button btnTasks;

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;

    @FXML
    private TableColumn<Task, String> nameCol;

    @FXML
    private TableColumn<Task, String> endCol;

    @FXML
    public void handleButtonClicks(ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnDashboard) {
            loadStage("Dashboard.fxml");
        } else if (mouseEvent.getSource() == btnStudents) {
            loadStage("task_display.fxml");
        } else if (mouseEvent.getSource() == btnTasks) {
            loadStage("TaskMaker.fxml");
        } else if (mouseEvent.getSource() == btnExit) {
            loadStage("exit.fxml");
        } else if (mouseEvent.getSource() == btnTodo) {
            loadStage("Todo.fxml");
        }
    }//Dashboard.fxml

    ObservableList<Task> TaskList = FXCollections.observableArrayList();

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

   // @Override
    //public void initialize(URL location, ResourceBundle resources) {
      //  timeNow();
   // }

    public void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            //String css = this.getClass().getResource("style.css").toExternalForm();
            //Stylesheet stylesheet = new Stylesheet(css);
            //stage.getStylesheets().add(css);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() throws SQLException {
        timeNow();
        connection = DBConnect.getConnect();
        // Set cell value factories once
        //idCol.setCellValueFactory(new PropertyValueFactory<>("TaskID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("TaskName"));
        //startCol.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        //statusCol.setCellValueFactory(new PropertyValueFactory<>("StatusTask"));
        //descCol.setCellValueFactory(new PropertyValueFactory<>("TaskDescription"));
        // ... set other cell value factories

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

    public void showExitScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ExitScreen.fxml"));
            Stage exitStage = new Stage();
            exitStage.setScene(new Scene(root));
            exitStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

