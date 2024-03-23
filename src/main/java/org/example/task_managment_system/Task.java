package org.example.task_managment_system;
import java.time.LocalDate;
import java.io.Serializable;
import java.io.Serializable;


public class Task implements Serializable {

        private String name;
        private String description; // Optional
        private static LocalDate startDate;
        private static LocalDate endDate;
        private static int totalTasks = 0;
        private static int completedTasks = 10;
        private static int remainderTasks = 10;
        private static int missedTasks = 10;


    public Task(String name, String description, LocalDate startDate, LocalDate endDate) {
            this.name = name;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            totalTasks++; // Update total tasks on object creation
    }

    public static void updateTaskCounts(Task task) {
        totalTasks++;
        // Update other task counts based on task properties (e.g., check dates for completed/missed)
        if (LocalDate.now().isAfter(task.getEndDate())) {
            if (task.getEndDate().isEqual(task.getStartDate())) {
                completedTasks++;
            } else {
                missedTasks++;
            }
        } else {
            remainderTasks++;
        }
    }
        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static LocalDate getStartDate() {
            return startDate;
        }

        public static LocalDate getEndDate() {
            return endDate;
        }

        @Override
        public String toString() {
            return  name + " - " + (startDate.equals(endDate) ? startDate.toString() : startDate + " to " + endDate);
        }

    public static int getTotalTasks() {  // remove static keyword
        return totalTasks;
    }

    public static int getCompletedTasks() {  // remove static keyword
        // Implement logic to check if task is completed based on dates and return 1 or 0
        if (LocalDate.now().isAfter(getEndDate())) {
            if (getStartDate().equals(getEndDate())) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int getRemainderTasks() {  // remove static keyword
        // Implement logic to check if task is a remainder based on dates and return 1 or 0
        if (LocalDate.now().isBefore(getEndDate())) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int getMissedTasks() {  // remove static keyword
        // Implement logic to check if task is missed based on dates and return 1 or 0
        if (LocalDate.now().isAfter(getEndDate()) && !getStartDate().equals(getEndDate())) {
            return 1;
        } else {
            return 0;
        }
    }
    }


