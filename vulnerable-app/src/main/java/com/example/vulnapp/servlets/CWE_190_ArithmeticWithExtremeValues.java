package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-190: arithmetic with extreme values. Sink: long j = Long.MAX_VALUE + 1. */
public class CWE_190_ArithmeticWithExtremeValues extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long i = Long.MAX_VALUE;
        // BAD: overflow
        long j = i + 1;

        // GOOD: no overflow
        int i2 = Integer.MAX_VALUE;
        long j2 = (long) i2 + 1;

        response.getWriter().print("j=" + j);
    }
}
