package com.example.vulnapp.servlets;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.KeyGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-326: insufficient key size, shown for RSA/DSA/DH/EC key pairs and AES secret keys exactly as
 * in the original snippet. Each sink is individually guarded only because some algorithms/curves
 * (e.g. AES with a 64-bit size, or the {@code secp112r1} curve) can be rejected by the JDK provider
 * at runtime; the vulnerable {@code initialize(...)}/{@code init(...)} calls are unchanged.
 */
public class CWE_326_InsufficientKeySizeBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            KeyPairGenerator keyPairGen1 = KeyPairGenerator.getInstance("RSA");
            keyPairGen1.initialize(1024); // BAD: Key size is less than 2048
        } catch (Exception e) { }

        try {
            KeyPairGenerator keyPairGen2 = KeyPairGenerator.getInstance("DSA");
            keyPairGen2.initialize(1024); // BAD: Key size is less than 2048
        } catch (Exception e) { }

        try {
            KeyPairGenerator keyPairGen3 = KeyPairGenerator.getInstance("DH");
            keyPairGen3.initialize(1024); // BAD: Key size is less than 2048
        } catch (Exception e) { }

        try {
            KeyPairGenerator keyPairGen4 = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp112r1"); // BAD: Key size is less than 256
            keyPairGen4.initialize(ecSpec);
        } catch (Exception e) { }

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(64); // BAD: Key size is less than 128
        } catch (Exception e) { }

        response.getWriter().print("key generators initialized");
    }
}
