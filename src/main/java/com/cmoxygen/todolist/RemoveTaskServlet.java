package com.cmoxygen.todolist;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RemoveTaskServlet", value = "/remove-task")
public class RemoveTaskServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("REMOVE TASK SERVLET INIT");
        DatabaseManager.connect();

        UserService.refreshDatabaseData();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("REMOVE TASK POST");

        HttpSession session = request.getSession(true);
        System.out.println(session.getId());

        StringBuilder received = new StringBuilder();
        String line;

        User userToResponse = new User();

        UserService.refreshDatabaseData();

        try {
            BufferedReader reader = request.getReader();

            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            System.out.println("RECEIVED POST DATA: ");
            System.out.println(received);

            if (!received.toString().isEmpty()) {

                UserProject up = UserService.searchProjectInDatabase(UserService.getTaskFromJson(received.toString()).getProjectId());

                if (up == null) {
                    System.out.println("UP IS NULL");
                    return;
                }

                UserTask ut = up.getProjectTaskById(UserService.getTaskFromJson(received.toString()).getTaskId());
                ut.removeFromDatabase();

                userToResponse = UserService.searchUserInDatabase(up.getUserId());

                if (userToResponse == null) {
                    System.out.println("USER TO RESPONSE IS NULL");
                    return;
                }

                userToResponse.refreshDatabaseData();
            }
        } catch (IOException e) {
            System.out.println("ERROR REQUEST");
            userToResponse = null;
        }

        UserService.refreshDatabaseData();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(UserService.userToJson(userToResponse));
        out.flush();
    }
}
