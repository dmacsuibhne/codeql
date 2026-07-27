package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-327: broken crypto algorithm (DES). Sink: Cipher.getInstance("DES"). */
public class CWE_327_BrokenCryptoAlgorithm extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("8bytekey".getBytes("UTF-8"), "DES");
            String input = request.getParameter("input");
            // BAD: DES is a weak algorithm
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));

            // GOOD: AES is a strong algorithm
            Cipher aes = Cipher.getInstance("AES");

            response.getWriter().print("encrypted " + encrypted.length + " bytes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
