package com.cmoxygen.todolist;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class UserService {

    private static ArrayList<User> users = new ArrayList<>();

    private static User userData = null;

    public static void refreshDatabaseData() {

        users.clear();

//        users = new User[];

        ArrayList<String> userIds = DatabaseManager.query("SELECT Users.id FROM to_do_list.Users;", 1);

        if (userIds == null)
            return;

//        users = new User[userIds.size()];

        for (String id : userIds) {

            User u = new User();
            u.getUserFromDatabase(Integer.parseInt(id));
            users.add(u);
        }

//        for (int i = 0; i < userIds.size(); i++) {
//
//            User u = new User();
//            u.getUserFromDatabase(Integer.parseInt(userIds.get(i)));
//            users[i] = u;
//        }
    }

    public static void setUserData(User u) {
        if (u != null) {
            userData = u;
        }
    }

    public static User getUserData() {
        return userData;
    }

    public static void clearUserData() {
        userData = null;
    }

    public static String userToJson(User u) {
        return u != null ? new Gson().toJson(u) : null;
    }

    public static String getAllUsersJson() {

        return new Gson().toJson(users);
    }

    public static void writeNewUserToDatabase(User u) {

        System.out.println("WRITE NEW USER TO DATABASE");
        u.writeToDatabase();
        u.writeUserProjectsToDatabase();
        u.writeNewProjectsToDatabase();
        u.displayAll();
    }

    public static User getUserFromJson(String json) {
        return new Gson().fromJson(json, User.class);
    }

    public static UserProject getProjectFromJson(String json) {
        return new Gson().fromJson(json, UserProject.class);
    }

    public static UserTask getTaskFromJson(String json) {
        return new Gson().fromJson(json, UserTask.class);
    }

    public static User searchUserInDatabase(User user) {

        if (user == null)
            return null;

//        user.generateHashPass();

        for (User databaseUser : users) {

            if (Objects.equals(user.getPassword(), databaseUser.getPassword())
                    && Objects.equals(user.getUsername(), databaseUser.getUsername())) {

                return databaseUser;
            }
        }
        return null;
    }

    public static User searchUserInDatabase(int userIdToSearch) {

        for (User u : users) {

            if (u.getId() == userIdToSearch) {
                return u;
            }
        }
        return null;
    }

    public static UserProject searchProjectInDatabase(int projectIdToSearch) {

        for (User u : users) {

            if (u.getUserProjectById(projectIdToSearch) != null) {
                return u.getUserProjectById(projectIdToSearch);
            }
        }
        return null;
    }

    public static User getTaskUser(UserTask ut) {

        if (ut == null)
            return null;

        return UserService.searchUserInDatabase(UserService.searchProjectInDatabase(ut.getProjectId()).getUserId());
    }
}
