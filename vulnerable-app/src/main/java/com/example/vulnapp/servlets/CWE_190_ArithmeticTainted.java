package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-190: tainted arithmetic overflow. Source: "data". Sink: int scaled = data * 10. */
public class CWE_190_ArithmeticTainted extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int data;
        String stringNumber = request.getParameter("data");
        if (stringNumber != null) {
            data = Integer.parseInt(stringNumber.trim());
        } else {
            data = 0;
        }
        // BAD: may overflow if input data is very large
        int scaled = data * 10;

        // GOOD: use a guard to ensure no overflows occur
        int scaled2;
        if (data < Integer.MAX_VALUE / 10)
            scaled2 = data * 10;
        else
            scaled2 = Integer.MAX_VALUE;

        response.getWriter().print("scaled=" + scaled);
    }
}
