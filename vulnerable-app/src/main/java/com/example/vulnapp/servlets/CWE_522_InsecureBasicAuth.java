package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpPost;
/** CWE-522 basic auth over HTTP. Sink: HttpPost(http url) + Authorization Basic header. */
public class CWE_522_InsecureBasicAuth extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // BAD: basic authentication over HTTP
        String url = "http://www.example.com/rest/getuser.do?uid=abcdx";

        // GOOD: basic authentication over HTTPS
        String secureUrl = "https://www.example.com/rest/getuser.do?uid=abcdx";

        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", "application/json");
        String authString = username + ":" + password;
        String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
        post.addHeader("Authorization", "Basic " + authStringEnc);
        response.getWriter().print("request prepared for " + post.getURI());
    }
}
