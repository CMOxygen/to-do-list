package com.cmoxygen.todolist;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DatabaseManager {

    private static final String url = "jdbc:mysql://localhost:3306/to_do_list";
    private static String user;
    private static String pass;

    private static Connection sql = null;
    private static Statement stmt = null;
    private static ResultSet rslt = null;

    public DatabaseManager() {

    }

    public static void connect() {
        if (!isConnected()) {
            try {
                File file = new File("/etc/server/d/u");
                byte[] dataBytes = Files.readAllBytes(file.toPath());

                String[] data = new String(dataBytes).split(",");

                if (data.length > 0) {
                    user = data[0];
                    pass = data[1];
                }
                try {
                    DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                    sql = DriverManager.getConnection(url, user, pass);
                    stmt = sql.createStatement();

                } catch (SQLException e) {
                    e.printStackTrace();
                    sql = null;
                    stmt = null;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void connect() {
//
//        if (!isConnected()) {
//
//            String[] data = new String(Objects.requireNonNull(EncryptionRSA.decrypt())).split(",");
//
//            try {
//
//                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
//                sql = DriverManager.getConnection(url, data[0], data[1]);
//                stmt = sql.createStatement();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                sql = null;
//                stmt = null;
//            }
//        }
//    }2

    public static void connect(String username, String password) {

        if (username != null && password != null && !isConnected()) {
            user = username;
            pass = password;

            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                sql = DriverManager.getConnection(url, user, pass);
                stmt = sql.createStatement();

            } catch (SQLException ex) {
                ex.printStackTrace();
                sql = null;
                stmt = null;
            }
        }
    }

    public static boolean isConnected() {
        return sql != null;
    }

    public static void update(String query) {

        if (sql != null && query != null) {

            try {
                stmt.executeUpdate(query);

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static ArrayList<String> query(String query, final int columns) {

        if (sql != null && query != null && columns > 0) {

            ArrayList<String> out = new ArrayList<>();

            try {
                rslt = stmt.executeQuery(query);

                while (rslt.next()) {

                    for (int i = 1; i <= columns; i++) {
                        out.add(rslt.getString(i));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
            return out;
        }
        return null;
    }

    public static String getSingleValue(String query) {

        if (sql != null & query != null) {
            String data = "";

            try {
                rslt = stmt.executeQuery(query);

                while (rslt.next()) {
                    data = rslt.getString(1);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }

            return data;
        }
        return null;
    }

    public static void close() {
        try {
            sql.close();
            sql = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            stmt.close();
            stmt = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            rslt.close();
            rslt = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}