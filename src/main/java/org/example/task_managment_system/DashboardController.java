package org.example.task_managment_system;

import org.example.task_managment_system.StudentsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<StudentsModel> tbData;
    @FXML
    public TableColumn<StudentsModel, String> Table_Task;

    @FXML
    public TableColumn<StudentsModel, String> Table_Start_Date;

    @FXML
    public TableColumn<StudentsModel, String> Table_End_Date;

    @FXML
    private PieChart pieChart;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadChart();
        loadStudents();
    }

    private void loadChart()
    {

        PieChart.Data slice1 = new PieChart.Data("Classes", 213);
        PieChart.Data slice2 = new PieChart.Data("Attendance"  , 67);
        PieChart.Data slice3 = new PieChart.Data("Teachers" , 36);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);

    }


    private ObservableList<StudentsModel> studentsModels = FXCollections.observableArrayList(
            new StudentsModel(Table_Task, Table_Start_Date, Table_End_Date)

    );

    private void loadStudents()
    {
        Table_Task.setCellValueFactory(new PropertyValueFactory<>("Task"));
        Table_Start_Date.setCellValueFactory(new PropertyValueFactory<>("Start Date"));
        Table_End_Date.setCellValueFactory(new PropertyValueFactory<>("End Date"));
        tbData.setItems(studentsModels);
    }

}
