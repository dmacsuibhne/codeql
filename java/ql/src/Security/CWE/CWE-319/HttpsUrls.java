package com.example.vulnapp.servlets;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-319: HTTP URL used where HTTPS is required. Sink: (HttpsURLConnection) u.openConnection() on http URL. */
public class CWE_319_HttpsUrls extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String protocol = "http://";
            URL u = new URL(protocol + "www.secret.example.org/");
            // BAD: HTTP URL cannot be used to make an HttpsURLConnection
            HttpsURLConnection hu = (HttpsURLConnection) u.openConnection();
            hu.setRequestMethod("PUT");
            response.getWriter().print("opened");
        } catch (Exception e) {
            response.getWriter().print("classcast reached");
        }
    }
}
