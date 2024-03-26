package org.example.task_managment_system;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeNow();
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

