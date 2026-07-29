package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-129: array size may be zero. Source: "numberOfItems". Sink: new String[n]; items[0]. */
public class CWE_129_ImproperValidationOfArrayConstruction extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int numberOfItems = Integer.parseInt(request.getParameter("numberOfItems").trim());
            if (numberOfItems >= 0) {
                // BAD: numberOfItems may be zero
                String[] items = new String[numberOfItems];
                items[0] = "Item 1";
            }

            if (numberOfItems > 0) {
                // GOOD numberOfItems must be greater than zero, so the indexing succeeds.
                String[] items = new String[numberOfItems];
                items[0] = "Item 1";
            }
            response.getWriter().print("ok");
        } catch (NumberFormatException e) {
        }
    }
}
