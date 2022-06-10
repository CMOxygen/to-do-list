package com.cmoxygen.todolist;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("LOGIN SERVLET INIT");
        DatabaseManager.connect();
        UserService.refreshDatabaseData();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("LOGIN SERVLET GET");

        HttpSession session = request.getSession(true);
        System.out.println(session.getId());

//        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("LOGIN SERVLET POST");

        HttpSession session = request.getSession(true);
        System.out.println(session.getId());

        UserService.refreshDatabaseData();

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
                userToResponse = UserService.searchUserInDatabase(UserService.getUserFromJson(received.toString()));
                userToResponse.setUserSession(session);

                response.addCookie(new Cookie("user-id", Integer.toString(userToResponse.getId())));
                response.addCookie(new Cookie("username", userToResponse.getUsername()));
                response.addCookie(new Cookie("password", userToResponse.getPassword()));
            }
        } catch (IOException e) {
            System.out.println("ERROR REQUEST");
            userToResponse = null;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(UserService.userToJson(userToResponse));
        out.flush();

//        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}