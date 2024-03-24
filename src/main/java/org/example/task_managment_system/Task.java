package org.example.task_managment_system;
import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {

        private String name;
        private String description; // Optional
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean completed;

        public Task(String name, String description, LocalDate startDate, LocalDate endDate) {
            this.name = name;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.completed = false;
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

        /*
        @Override
        public String toString() {
            return  name + " - " + (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
        }
        */



    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    @Override
    public String toString() {
        String completionStatus = isCompleted() ? "[COMPLETED]" : "";
        return completionStatus + "  " + getName() + " - " + (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
    }

}

