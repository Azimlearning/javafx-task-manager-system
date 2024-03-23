package org.example.task_managment_system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

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
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<Task> tbData;
    @FXML
    public TableColumn<Task, String> Table_Task;

    @FXML
    public TableColumn<Task, String> Table_Start_Date;

    @FXML
    public TableColumn<Task, String> Table_End_Date;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalTaskLabel; // Label to display total tasks

    @FXML
    private Label completedTaskLabel; // Label to display completed tasks

    @FXML
    private Label remainderTaskLabel; // Label to display remainder tasks

    @FXML
    private Label missedTaskLabel; // Label to display missed tasks

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadTasks();  // Read tasks from file on initialization
        updateTaskLabels();  // Update labels with task counts
        loadChart();

    }

    private void loadTasks() {
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

        Table_Task.setCellValueFactory(new PropertyValueFactory<>("name"));
        Table_Start_Date.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        Table_End_Date.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tbData.setItems(tasks);
    }

    private void updateTaskLabels() {
        totalTaskLabel.setText(String.valueOf(Task.getTotalTasks()));
        completedTaskLabel.setText(String.valueOf(Task.getCompletedTasks()));
        remainderTaskLabel.setText(String.valueOf(Task.getRemainderTasks()));
        missedTaskLabel.setText(String.valueOf(Task.getMissedTasks()));
    }

    private void loadChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalTasks = Task.getTotalTasks();
        if (totalTasks > 0) {
            pieChartData.add(new PieChart.Data("Completed", Task.getCompletedTasks()));
            pieChartData.add(new PieChart.Data("Remainder", Task.getRemainderTasks()));
            pieChartData.add(new PieChart.Data("Missed", Task.getMissedTasks()));
        }
        pieChart.setData(pieChartData);
    }
}