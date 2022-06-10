package com.cmoxygen.todolist;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CreateProjectServlet", value = "/create-project")
public class CreateProjectServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("CREATE PROJECT SERVLET INIT");
        DatabaseManager.connect();

        UserService.refreshDatabaseData();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CREATE PROJECT POST");

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
                userToResponse = UserService.searchUserInDatabase(UserService.getProjectFromJson(received.toString()).getUserId());
                userToResponse.createUserProject(UserService.getProjectFromJson(received.toString())).addToDatabase();
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
