package com.example.vulnapp.servlets;
import java.io.IOException;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-798 hardcoded credentials in an API call. Sink: DriverManager.getConnection(url, u, p). */
public class CWE_798_HardcodedCredentialsApiCall extends HttpServlet {
    private static final String p = "123456"; // BAD: hard-coded credential
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "jdbc:mysql://localhost/test";
        String u = "admin"; // BAD: hard-coded credential
        try {
            DriverManager.getConnection(url, u, p); // sensitive call
            response.getWriter().print("connected");
        } catch (Exception e) {
            response.getWriter().print("connection attempted");
        }
    }
}
