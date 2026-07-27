package com.example.vulnapp.servlets;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-319: cleartext HTTP output stream. Sink: httpcon.getOutputStream() on http URL. */
public class CWE_319_UseSSL extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            URL u = new URL("http://www.secret.example.org/");
            HttpURLConnection httpcon = (HttpURLConnection) u.openConnection();
            httpcon.setRequestMethod("PUT");
            httpcon.setDoOutput(true);
            // BAD: output stream from non-HTTPS connection
            OutputStream os = httpcon.getOutputStream();
            response.getWriter().print("opened stream");
        } catch (Exception e) {
            response.getWriter().print("stream attempted");
        }
    }
}
