package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-129: index may be -1. Source: "productSearchTerm". Sink: productDescriptions[foundProductID]. */
public class CWE_129_ImproperValidationOfArrayIndexCodeSpecified extends HttpServlet {
    private String[] productDescriptions = new String[]{"Chocolate bar", "Fizzy drink"};
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("productSearchTerm");
        int foundProductID = -1;
        for (int i = 0; i < productDescriptions.length; i++) {
            if (productDescriptions[i].contains(searchTerm)) {
                foundProductID = i;
                break;
            }
        }
        // BAD: foundProductID may be -1
        response.getWriter().write(productDescriptions[foundProductID]);
    }
}
