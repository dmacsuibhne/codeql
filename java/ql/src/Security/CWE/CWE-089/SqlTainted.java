package com.example.vulnapp.servlets;

import com.example.vulnapp.Backends;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-089: SQL Injection.
 * Source: request parameter "category". Sink: statement.executeQuery(query1).
 * Root cause: untrusted input concatenated into a SQL statement.
 */
public class CWE_089_SqlTainted extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // The category might have SQL special characters in it
        String category = request.getParameter("category");
        PrintWriter out = response.getWriter();
        try (Connection connection = Backends.getConnection()) {
            Statement statement = connection.createStatement();
            // BAD: the category is concatenated directly into the query
            String query1 = "SELECT ITEM,PRICE FROM PRODUCT WHERE ITEM_CATEGORY='"
                    + category + "' ORDER BY PRICE";
            ResultSet results = statement.executeQuery(query1);
            while (results.next()) {
                out.println(results.getString("ITEM") + " : " + results.getInt("PRICE"));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

