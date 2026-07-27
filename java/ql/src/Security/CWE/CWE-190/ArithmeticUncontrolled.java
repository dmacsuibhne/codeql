package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-190: uncontrolled arithmetic overflow. Sink: int scaled = data * 10 (data from SecureRandom). */
public class CWE_190_ArithmeticUncontrolled extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int data = (new java.security.SecureRandom()).nextInt();
        // BAD: may overflow if data is large
        int scaled = data * 10;
        response.getWriter().print("scaled=" + scaled);
    }
}
