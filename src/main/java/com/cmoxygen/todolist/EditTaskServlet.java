package com.cmoxygen.todolist;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EditTaskServlet", value = "/edit-task")
public class EditTaskServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("EDIT TASK SERVLET INIT");
        DatabaseManager.connect();

        UserService.refreshDatabaseData();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("EDIT TASK POST");

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
                ut.changeTaskProperties(UserService.getTaskFromJson(received.toString()));

                userToResponse = UserService.searchUserInDatabase(up.getUserId());

            }
        } catch (IOException e) {
            System.out.println("ERROR REQUEST");
            userToResponse = null;
        }

        UserService.refreshDatabaseData();

//        System.out.println("USER TO RESPONSE DISPLAY ALL");
//        assert userToResponse != null;
//        userToResponse.displayAll();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(UserService.userToJson(userToResponse));
        out.flush();
    }
}
