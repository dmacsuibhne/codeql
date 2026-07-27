package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-780: RSA without OAEP padding. Sink: Cipher.getInstance("RSA/ECB/NoPadding"). */
public class CWE_780_RsaWithoutOaep extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // BAD: No padding scheme is used
            Cipher rsa = Cipher.getInstance("RSA/ECB/NoPadding");
            response.getWriter().print("cipher=" + rsa.getAlgorithm());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
