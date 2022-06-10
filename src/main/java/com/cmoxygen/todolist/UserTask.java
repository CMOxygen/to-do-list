package com.cmoxygen.todolist;

import java.util.Objects;

public class UserTask {

    private final int taskTitleMaxLength = 75;
    private final int taskTextMaxLength = 150;

    private int taskId = 0;
    private int projectId = 0;
    private String taskTitle = "";
    private String taskText = null;
    private String taskStatus = "not_completed";
    private String deadline = null;
    private int priority = 4;

    public UserTask(int project_id, String title) {

        if (!title.isEmpty() && title.length() <= taskTitleMaxLength) {

            projectId = project_id;
            taskTitle = title;
        }
    }

    public UserTask() {

    }

    public int getProjectId() {
        return projectId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setTaskText(String text) {
        if (text.length() <= taskTextMaxLength)
            taskText = text;
    }

    public void removeFromDatabase() {

        if (DatabaseManager.isConnected() && taskId > 0) {

            if (DatabaseManager.query(
                    "SELECT UserTasks.taskId FROM to_do_list.UserTasks WHERE taskId=" + taskId + ";", 1).isEmpty()) {

                System.out.println("removeTaskFromDatabase NO SUCH ID");
                return;
            }

            DatabaseManager.update("DELETE FROM to_do_list.UserTasks WHERE UserTasks.taskId=" + taskId + ";");
        }
    }

    public void getTaskFromDatabase(int databaseId) {

        if (databaseId > 0 && DatabaseManager.isConnected()) {

            taskId = databaseId;

            projectId = Integer.parseInt(
                    Objects.requireNonNull(DatabaseManager.getSingleValue("SELECT UserTasks.projectId FROM to_do_list.UserTasks"
                            + " WHERE UserTasks.taskId=" + databaseId + ";")));

            taskTitle = DatabaseManager.getSingleValue("SELECT UserTasks.taskTitle FROM to_do_list.UserTasks"
                    + " WHERE UserTasks.taskId=" + databaseId + ";");

            taskText = DatabaseManager.getSingleValue("SELECT UserTasks.taskText FROM to_do_list.UserTasks"
                    + " WHERE UserTasks.taskId=" + databaseId + ";");

            taskStatus = DatabaseManager.getSingleValue("SELECT UserTasks.taskStatus FROM to_do_list.UserTasks"
                    + " WHERE UserTasks.taskId=" + databaseId + ";");

            deadline = DatabaseManager.getSingleValue("SELECT UserTasks.deadline FROM to_do_list.UserTasks"
                    + " WHERE UserTasks.taskId=" + databaseId + ";");

            priority = Integer.parseInt(
                    Objects.requireNonNull(DatabaseManager.getSingleValue("SELECT UserTasks.priority FROM to_do_list.UserTasks"
                            + " WHERE UserTasks.taskId=" + databaseId + ";")));
        }
    }

    public void addToDatabase() {
        if (DatabaseManager.isConnected() && !taskTitle.isEmpty() && taskTitle.length() <= taskTitleMaxLength) {

            taskTitle = "'" + taskTitle + "'";

            if (taskText != null)
                taskText = "'" + taskText + "'";

            if (taskStatus != null)
                taskStatus = "'" + taskStatus + "'";

            if (deadline != null)
                deadline = "'" + deadline + "'";

            DatabaseManager.update("INSERT INTO to_do_list.UserTasks(projectId, taskTitle, taskText, taskStatus, deadline, priority)"
                    + " VALUES(" + projectId + ", " + taskTitle + ", " + taskText + ", " + taskStatus + ", " + deadline + ", " + priority + ");");

            taskId = Integer.parseInt(
                    Objects.requireNonNull(DatabaseManager.getSingleValue("SELECT COUNT(*) FROM to_do_list.UserTasks;")));
        }
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskText() {
        return taskText;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public int getPriority() {
        return priority;
    }


    public void display() {

        System.out.println("TASK ID=" + taskId + " DISPLAY");

        System.out.println(taskId);
        System.out.println(projectId);
        System.out.println(taskTitle);
        System.out.println(taskText);
        System.out.println(taskStatus);
        System.out.println(deadline);
        System.out.println(priority);
    }

    public UserTask changeTaskProperties(UserTask ut) {

        if (ut != null) {

            changeTaskTitle(ut.getTaskTitle());
            changeTaskText(ut.getTaskText());
            changeTaskDeadline(ut.getDeadline());
            changeTaskStatus(ut.getTaskStatus());
            changeTaskPriority(ut.getPriority());
        }
        return this;
    }

    public void changeTaskTitle(String title) {

        if (DatabaseManager.isConnected() && !title.isEmpty() && title.length() < taskTitleMaxLength) {
            taskTitle = title;
            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.taskTitle='" + taskTitle
                    + "' WHERE UserTasks.taskId=" + taskId + ";");
        }
    }

    public void changeTaskText(String text) {
        if (DatabaseManager.isConnected() && text != null && text.length() < taskTextMaxLength) {

            taskText = text;

            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.taskText='" + taskText + "' WHERE UserTasks.taskId=" + taskId + ";");
        }
        if (DatabaseManager.isConnected() && text == null) {

            taskText = text;
            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.taskText=" + taskText + " WHERE UserTasks.taskId=" + taskId + ";");

        }
    }

    public void changeTaskStatus(String status) {
        if (DatabaseManager.isConnected() && !status.isEmpty()) {
            taskStatus = status;
            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.taskStatus='" + taskStatus
                    + "' WHERE UserTasks.taskId=" + taskId + ";");
        }
    }

    public void changeTaskDeadline(String dl) {

        if (DatabaseManager.isConnected()) {
            deadline = dl;

            if (deadline != null)
                deadline = "'" + deadline + "'";

            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.deadline=" + deadline
                    + " WHERE UserTasks.taskId=" + taskId + ";");
        }
    }

    public void changeTaskPriority(int pr) {

        if (DatabaseManager.isConnected() && pr > 0 && pr <= 4) {

            priority = pr;

            DatabaseManager.update("UPDATE to_do_list.UserTasks SET UserTasks.priority=" + priority
                    + " WHERE UserTasks.taskId=" + taskId + ";");
        }
    }

    public int getTaskId() {
        return taskId;
    }

}
