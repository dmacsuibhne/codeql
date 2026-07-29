package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-129: code-specified array size may be zero. Sink: new String[n]; items[0]. */
public class CWE_129_ImproperValidationOfArrayConstructionCodeSpecified extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int numberOfItems = new Random().nextInt(10);
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
    }
}
