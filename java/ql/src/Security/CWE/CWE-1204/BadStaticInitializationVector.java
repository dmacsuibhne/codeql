package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-1204: static (all-zero) initialization vector. Sink: cipher.init with constant IV. */
public class CWE_1204_BadStaticInitializationVector extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            java.security.Key key = KeyGenerator.getInstance("AES").generateKey();
            byte[] iv = new byte[16]; // BAD: all zeroes
            GCMParameterSpec params = new GCMParameterSpec(128, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, params);
            response.getWriter().print("cipher initialized");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
