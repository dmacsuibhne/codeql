package com.example.vulnapp.servlets;
import com.example.vulnapp.Backends;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-089: concatenated SQL. Source: "category". Sink: statement.executeQuery(query1). */
public class CWE_089_SqlConcatenated extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String category = request.getParameter("category");
        try (Connection connection = Backends.getConnection()) {
            Statement statement = connection.createStatement();
            // BAD: the category might have SQL special characters in it
            String query1 = "SELECT ITEM,PRICE FROM PRODUCT WHERE ITEM_CATEGORY='"
                    + category + "' ORDER BY PRICE";
            ResultSet results = statement.executeQuery(query1);
            int n = 0;
            while (results.next()) { n++; }

            // GOOD: use a prepared query
            String query2 = "SELECT ITEM,PRICE FROM PRODUCT WHERE ITEM_CATEGORY=? ORDER BY PRICE";
            PreparedStatement prepared = connection.prepareStatement(query2);
            prepared.setString(1, category);
            ResultSet safeResults = prepared.executeQuery();

            response.getWriter().print("rows=" + n);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
