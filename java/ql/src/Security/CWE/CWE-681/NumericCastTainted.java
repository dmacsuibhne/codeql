package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-681: numeric truncation of tainted value. Source: "data". Sink: int scaled = (int) data. */
public class CWE_681_NumericCastTainted extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long data;
        String stringNumber = request.getParameter("data");
        if (stringNumber != null) {
            data = Long.parseLong(stringNumber.trim());
        } else {
            data = 0;
        }
        // AVOID: potential truncation if input data is very large
        int scaled = (int) data;
        response.getWriter().print("scaled=" + scaled);
    }
}
