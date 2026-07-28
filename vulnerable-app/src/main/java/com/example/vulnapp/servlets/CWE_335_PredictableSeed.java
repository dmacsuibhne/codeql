package com.example.vulnapp.servlets;
import java.io.IOException;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-335: predictable seed. Sink: prng.setSeed(12345L). */
public class CWE_335_PredictableSeed extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SecureRandom prng = new SecureRandom();
        // BAD: constant seed makes output predictable
        prng.setSeed(12345L);
        int randomData = prng.nextInt();

        // BAD: System.currentTimeMillis() returns the system time which is predictable.
        prng.setSeed(System.currentTimeMillis());
        randomData = prng.nextInt();

        // GOOD: SecureRandom implementations seed themselves securely by default.
        prng = new SecureRandom();
        randomData = prng.nextInt();

        response.getWriter().print("value=" + randomData);
    }
}
