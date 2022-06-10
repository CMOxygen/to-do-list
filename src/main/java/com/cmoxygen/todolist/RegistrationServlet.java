package com.cmoxygen.todolist;

import com.google.gson.Gson;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "RegistrationServlet", value = "/reg")
public class RegistrationServlet extends HttpServlet {

    public void init() {

        System.out.println("REG SERVLET INIT");
        DatabaseManager.connect();
        System.out.println("IS CONNECTED = " + DatabaseManager.isConnected());

        UserService.refreshDatabaseData();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("REG SERVLET GET");

        HttpSession session = request.getSession(true);
        System.out.println(session.getId());

        UserService.refreshDatabaseData();

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");

//        PrintWriter out = response.getWriter();
//        out.print(UserService.getAllUsersJson());
//        out.println(new Gson().toJson(UserService.testUser()));
//        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("REG SERVLET POST");

        HttpSession session = request.getSession(true);
        System.out.println(session.getId());

        StringBuilder received = new StringBuilder();
        String line;

        User userToResponse = new User();

        try {
            BufferedReader reader = request.getReader();

            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            System.out.println("RECEIVED POST DATA: ");
            System.out.println(received);

            if (!received.toString().isEmpty()) {
                UserService.writeNewUserToDatabase(UserService.getUserFromJson(received.toString()));
                UserService.refreshDatabaseData();

                userToResponse = UserService.searchUserInDatabase(UserService.getUserFromJson(received.toString()));

                response.addCookie(new Cookie("user-id", Integer.toString(userToResponse.getId())));
                response.addCookie(new Cookie("username", userToResponse.getUsername()));
                response.addCookie(new Cookie("password", userToResponse.getPassword()));
            }
        } catch (IOException e) {
            System.out.println("ERROR REQUEST");
        }
        UserService.refreshDatabaseData();

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        PrintWriter out = response.getWriter();
//        out.print();
//        out.flush();
    }

    public void destroy() {
        System.out.println("REG SERVLET DESTROY");
    }
}