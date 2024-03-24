package org.example.task_managment_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button btnDashboard;

    @FXML
    public Button btnStudents;

    @FXML
    public Button btn_Timetable;

    @FXML
    public Button btnSettings;

    @FXML
    public Button btnUpdate;

    @FXML
    public Button btnTasks;

    //my bad - the freaking mouse event
    @FXML
    public void handleButtonClicks(ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnDashboard) {
            loadStage("Dashboard.fxml");
        } else if (mouseEvent.getSource() == btnStudents) {
            loadStage("task_display.fxml");
        } else if (mouseEvent.getSource() == btnTasks) {
            loadStage("TaskMaker.fxml");
        }
    }//Dashboard.fxml


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

