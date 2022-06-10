package com.cmoxygen.todolist;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class User {

    private final int maxNameLength = 20;
    transient private final int maxHashLength = 90;
    private final int maxPassLength = 15;

    private int id = 0;
    private String username = "";
    private String password = "";

    private ArrayList<UserProject> userProjects = new ArrayList<>();
    transient private ArrayList<UserProject> newUserProjects = new ArrayList<>();

    transient private HttpSession userSession = null;

    protected User() {
    }

    public User(String newUsername, String newPassword) {

        if (!newUsername.isEmpty() && !newPassword.isEmpty() && newUsername.length() <= maxNameLength && newPassword.length() <= maxHashLength) {

            username = newUsername;
            password = newPassword;
        }
    }

    protected User(User u) {

        if (u != null && !u.username.isEmpty() && !u.password.isEmpty() && u.id > 0 && u.userProjects != null && u.username.length() <= maxNameLength && u.password.length() <= maxHashLength) {

            this.id = u.id;
            this.username = u.username;
            this.password = u.password;
            this.userProjects = u.userProjects;
        }
    }

    public void writeToDatabase() {

        if (DatabaseManager.isConnected() && !username.isEmpty() && !password.isEmpty() && id == 0) {

//            byte[] passBytes = password.getBytes(StandardCharsets.UTF_8);
//            password = Base64.getEncoder().encodeToString(HashSHA.generateHash(passBytes));

            DatabaseManager.update("INSERT INTO to_do_list.Users(username, password) VALUES ('" + username + "', '" + password + "')");
            id = Integer.parseInt(
                    Objects.requireNonNull(DatabaseManager.getSingleValue("SELECT COUNT(*) FROM to_do_list.Users;")));

            for (UserProject up : newUserProjects) {
                up.setUserId(id);
            }

            for (UserProject up : userProjects) {
                up.setUserId(id);
            }
        }
    }

    public void generateHashPass() {

        if (!password.isEmpty() && id == 0) {

            byte[] passBytes = password.getBytes(StandardCharsets.UTF_8);
            password = Base64.getEncoder().encodeToString(HashSHA.generateHash(passBytes));
        }
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<UserProject> getUserProjects() {
        return userProjects;
    }

    public void writeNewProjectsToDatabase() {

        for (UserProject up : newUserProjects) {
            up.addToDatabase();
            up.addNewTasksToDatabase();
        }
    }

    public void writeUserProjectsToDatabase() {

        for (UserProject up : userProjects) {
            up.addToDatabase();
            up.addNewTasksToDatabase();
        }
    }

    public UserProject getUserProjectById(int projectId) {

        for (UserProject up : userProjects) {
            if (up.getProjectId() == projectId)
                return up;
        }
        return null;
    }

    public UserProject createUserProject(String name) {

        UserProject up = new UserProject(id, name);
        newUserProjects.add(up);
        return up;
    }

    public UserProject createUserProject(UserProject up) {

        newUserProjects.add(up);
        return up;
    }

    public ArrayList<UserProject> getNewUserProjects() {
        return newUserProjects;
    }

    public void getUserFromDatabase(int databaseId) {

        if (databaseId > 0 && DatabaseManager.isConnected()) {

            userProjects.clear();
            newUserProjects.clear();

            id = databaseId;
            username = DatabaseManager.getSingleValue("SELECT Users.username FROM to_do_list.Users WHERE Users.id=" + id + ";");
            password = DatabaseManager.getSingleValue("SELECT Users.password FROM to_do_list.Users WHERE Users.id=" + id + ";");

            ArrayList<String> projectIds = DatabaseManager.query("SELECT UserProjects.projectId FROM to_do_list.UserProjects WHERE UserProjects.userId=" + id + ";", 1);

            if (projectIds == null)
                return;

            for (String s : projectIds) {
                UserProject up = new UserProject();
                up.getProjectFromDatabase(Integer.parseInt(s));
                userProjects.add(up);
            }
        }
    }

    public void setUserSession(HttpSession session) {
        if (session != null)
            userSession = session;
    }

    public int getId() {
        return id;
    }

    public void refreshDatabaseData() {
        this.getUserFromDatabase(this.id);
    }

    public void display() {

        System.out.println(id);
        System.out.println(username);
        System.out.println(password);
    }

    public void displayAll() {

        System.out.println("USER ID=" + id + " DISPLAY ALL");

        System.out.println(id);
        System.out.println(username);
        System.out.println(password);

        for (UserProject up : userProjects) {
            up.displayAll();
        }
    }
}
