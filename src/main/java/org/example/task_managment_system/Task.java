package org.example.task_managment_system;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {

        private int TaskID;
        private String name;
        private String description; // Optional
        private LocalDate startDate;
        private LocalDate endDate;
        private String StatusTask;
        private boolean completed;
        private boolean onGoing;
        private boolean missed;


        public Task(int TaskID, String name, String description, LocalDate startDate, LocalDate endDate, String StatusTask) {
            this.TaskID= TaskID;
            this.name = name;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.completed = false;
            this.onGoing = true;
            this.missed = isStartDatePassed(); // Check for missed status on creation
            this.StatusTask = StatusTask;

        }

        public Integer getID() {return TaskID;}

        public int getTaskID() {return TaskID;}

        public String getName() {return name;}

        public String getDescription() {return description;}

        public LocalDate getStartDate() {return startDate;}

        public LocalDate getEndDate() {return endDate;}

        public String getStatusTask(){
            return StatusTask;
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

    public boolean isOnGoing() {
        return onGoing && !isMissed(); // Updated to consider missed state
    }


    public boolean isMissed() {
        return missed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.onGoing = !completed; // Set onGoing directly based on completed
    }



    public void setIsOngoing(boolean isOngoing) {
        if (isOngoing) {
            // Ensure ongoing state is consistent with completed and missed
            this.completed = false;
            this.missed = isStartDatePassed() && !isCompleted(); // Recheck missed
        }
        this.onGoing = isOngoing;
    }

    private boolean isStartDatePassed() {
        return LocalDate.now().isAfter(startDate); // Check if current date is past start date
    }
    public void updateTaskStatus() {//You can call this method whenever you need to refresh the task status based on the current date
        // Update missed and ongoing flags based on current date
        this.missed = isStartDatePassed() && !isCompleted();
        this.onGoing = !isCompleted() && !isMissed();
    }
    /*no usage
    public void uncompleteTask(ActionEvent event) {
        Task selectedTask = eventList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(false);
            selectedTask.updateTaskStatus(); // Call to recalculate onGoing
            eventList.refresh();
        } else {
            // ... (error message)
        }
    }
    */



    /*
    @Override
    public String toString() {
        String completionStatus = isCompleted() ? "[COMPLETED]" : "";
        return completionStatus + "  " + getName() + " - " + (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
    */
    @Override
    public String toString() {
        String statusString;
        if (isCompleted()) {
            statusString = "[COMPLETED]";
        } else if (isOnGoing()) {
            statusString = "[ONGOING]";
        } else if (isMissed()) {
            statusString = "[MISSED]";
        } else {
            statusString = "";  // Handle any other potential cases
        }

        return "[" + getStatusTask() + "]" + " " + getName() + " - " + (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


}


