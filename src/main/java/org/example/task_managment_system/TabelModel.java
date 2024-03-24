package org.example.task_managment_system;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

public class TabelModel {

    private SimpleIntegerProperty tableTask;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;

    public TabelModel(TableColumn<TabelModel, String> tableTask, TableColumn<TabelModel, String> startDate, TableColumn<TabelModel, String> endDate) {
        this.tableTask = new SimpleIntegerProperty();
        this.startDate = new SimpleStringProperty();
        this.endDate = new SimpleStringProperty();
    }
/*
    public string getStudentId() {
        return tableTask.get();
    }

    public void setStudentId(int studentId) {
        this.studentId = new SimpleIntegerProperty(studentId);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName = new SimpleStringProperty(lastName);
    }



 */
}