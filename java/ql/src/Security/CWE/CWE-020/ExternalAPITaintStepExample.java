package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-020: untrusted data concatenated into a SQL string. Source: "user_id". Sink: StringBuilder query. */
public class CWE_020_ExternalAPITaintStepExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append("SELECT * FROM user WHERE user_id='");
        // BAD: a request parameter is concatenated directly into a SQL query
        sqlQueryBuilder.append(request.getParameter("user_id"));
        sqlQueryBuilder.append("'");
        response.getWriter().print(sqlQueryBuilder.toString());
    }
}
