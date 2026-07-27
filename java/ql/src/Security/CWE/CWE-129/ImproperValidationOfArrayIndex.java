package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-129: unvalidated array index. Source: "productID". Sink: productDescriptions[productID]. */
public class CWE_129_ImproperValidationOfArrayIndex extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] productDescriptions = new String[]{"Chocolate bar", "Fizzy drink"};
        String userProperty = request.getParameter("productID");
        try {
            int productID = Integer.parseInt(userProperty.trim());
            // BAD: array accessed without bounds check
            String productDescription = productDescriptions[productID];
            response.getWriter().write(productDescription);
        } catch (NumberFormatException e) {
        }
    }
}
