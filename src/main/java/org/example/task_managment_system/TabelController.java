package org.example.task_managment_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TabelController implements Initializable {

    @FXML
    private TableView<TabelModel> tbData;
    @FXML
    public TableColumn<TabelModel, Integer> studentId;

    @FXML
    public TableColumn<TabelModel, String> firstName;

    @FXML
    public TableColumn<TabelModel, String> lastName;

    @FXML
    public Button btnentertask;

    @FXML
    public TextField btntask;

    @FXML
    public TextField btn_start_date;

    @FXML
    public TextField btn_due_date;


    String AddTask;
    int StartDate;
    int DueDate;

    public void submit(ActionEvent event){
        AddTask = btntask.getText();
        StartDate = Integer.parseInt(btn_start_date.getText());
        DueDate = Integer.parseInt(btn_due_date.getText());
        System.out.println(AddTask + StartDate + DueDate);
    }

    public TabelController()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        studentId.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tbData.setItems(studentsModels);
    }


    private ObservableList<TabelModel> studentsModels = FXCollections.observableArrayList(

    );



}
