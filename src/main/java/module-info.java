module org.example.task_managment_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.task_managment_system to javafx.fxml;
    exports org.example.task_managment_system;
}