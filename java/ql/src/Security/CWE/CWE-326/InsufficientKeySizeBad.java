package com.example.vulnapp.servlets;
import java.io.IOException;
import java.security.KeyPairGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-326: insufficient key size. Sink: keyPairGen.initialize(1024). */
public class CWE_326_InsufficientKeySizeBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            KeyPairGenerator keyPairGen1 = KeyPairGenerator.getInstance("RSA");
            keyPairGen1.initialize(1024); // BAD: Key size is less than 2048
            response.getWriter().print("key generator initialized");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
