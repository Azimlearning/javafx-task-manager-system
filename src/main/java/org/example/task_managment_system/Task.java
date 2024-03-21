package org.example.task_managment_system;
import java.time.LocalDate;
public class Task {
    private String name;
    private String description; // Optional
    private LocalDate startDate;
    private LocalDate endDate;

    public Task(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return  name + " - " +
                (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
    }
}