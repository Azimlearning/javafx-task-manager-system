package org.example.task_managment_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ExitController {

    @FXML
    private Button confirmExitButton;

    @FXML
    protected void handleExit() {
        Stage stage = (Stage) confirmExitButton.getScene().getWindow();
        stage.close();
    }
}
