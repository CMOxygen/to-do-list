package com.cmoxygen.todolist;

import java.util.ArrayList;

public class UserProject {

    private final int maxProjectNameLength = 20;

    private int projectId = 0;
    private int userId = 0;
    private String projectName = "defaultProject";

    private ArrayList<UserTask> projectTasks = new ArrayList<>();
    transient private ArrayList<UserTask> newProjectTasks = new ArrayList<>();

    public UserProject(int user_id, String name) {

        if (user_id > 0 && !name.isEmpty() && name.length() <= maxProjectNameLength) {
            userId = user_id;
            projectName = name;
        }
    }

    public int getUserId() {
        return userId;
    }

    public UserProject() {

    }

    public boolean hasTasks() {
        return !projectTasks.isEmpty();
    }

    public UserProject(UserProject up) {

        if (up.projectId > 0 && up.userId > 0 && up.projectTasks != null && !up.projectName.isEmpty() && up.projectName.length() <= maxProjectNameLength) {

            this.projectId = up.projectId;
            this.userId = up.userId;
            this.projectName = up.projectName;
            this.projectTasks = up.projectTasks;
        }
    }

    public ArrayList<UserTask> getNewProjectTasks() {
        return newProjectTasks;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void changeProjectName(String newProjectName) {

        if (DatabaseManager.isConnected() && projectId > 0 && userId > 0 && !newProjectName.isEmpty()
                && newProjectName.length() <= maxProjectNameLength) {

            projectName = newProjectName;
            DatabaseManager.update("UPDATE to_do_list.UserProjects SET UserProjects.projectName='"
                    + projectName + "'" + " WHERE UserProjects.projectId=" + projectId + ";");
        }
    }

    public void removeFromDatabase() {

        if (DatabaseManager.isConnected() && projectId > 0) {

            if (DatabaseManager.query("SELECT UserProjects.projectId FROM to_do_list.UserProjects WHERE UserProjects.projectId="
                    + projectId + ";", 1).isEmpty()) {

                System.out.println("removeFromDatabase NO SUCH ID");
                return;
            }

            if (!projectTasks.isEmpty()) {

                for (UserTask ut : projectTasks) {
                    ut.removeFromDatabase();
                }
            }
            DatabaseManager.update("DELETE FROM to_do_list.UserProjects WHERE UserProjects.projectId=" + projectId + ";");
        }
    }

    public void addToDatabase() {

        if (DatabaseManager.isConnected()) {
            DatabaseManager.update("INSERT INTO to_do_list.UserProjects(userId, projectName) VALUES(" + userId + ", '" + projectName + "');");
            projectId = Integer.parseInt(DatabaseManager.getSingleValue("SELECT COUNT(*) FROM to_do_list.UserProjects;"));

            for (UserTask ut : newProjectTasks) {
                ut.setProjectId(projectId);
            }
        }
    }

    public void refreshDatabaseData() {
        this.getProjectFromDatabase(this.projectId);
    }

    public UserTask getProjectTaskById(int taskId) {

        for (UserTask ut : projectTasks) {
            if (ut.getTaskId() == taskId) return ut;
        }
        return null;
    }

    public UserTask createUserTask(String title) {

        UserTask ut = new UserTask(projectId, title);
        newProjectTasks.add(ut);
        return ut;
    }

    public UserTask createUserTask(UserTask ut) {

        newProjectTasks.add(ut);
        return ut;
    }

    public void addNewTasksToDatabase() {

        for (UserTask ut : newProjectTasks) {

            ut.addToDatabase();
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public void getProjectFromDatabase(int databaseId) {

        if (databaseId > 0 && DatabaseManager.isConnected()) {

            newProjectTasks.clear();
            projectTasks.clear();

            projectId = databaseId;
            userId = Integer.parseInt(DatabaseManager.getSingleValue("SELECT UserProjects.userId FROM to_do_list.UserProjects" + " WHERE UserProjects.projectId=" + projectId + ";"));
            projectName = DatabaseManager.getSingleValue("SELECT UserProjects.projectName FROM to_do_list.UserProjects" + " WHERE UserProjects.projectId=" + projectId + ";");

            ArrayList<String> taskIdsBuffer = DatabaseManager.query("SELECT UserTasks.taskId FROM to_do_list.UserTasks" + " WHERE UserTasks.projectId=" + databaseId + ";", 1);

            for (String s : taskIdsBuffer) {

                UserTask ut = new UserTask();
                ut.getTaskFromDatabase(Integer.parseInt(s));
                projectTasks.add(ut);
            }
        }
    }

    public void display() {

        System.out.println(projectId);
        System.out.println(userId);
        System.out.println(projectName);
    }

    public void displayAll() {

        System.out.println("PROJECT ID=" + projectId + " DISPLAY ALL");

        System.out.println(projectId);
        System.out.println(userId);
        System.out.println(projectName);

        for (UserTask ut : projectTasks) {
            ut.display();
        }
    }
}
