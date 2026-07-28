package com.example.vulnapp.servlets;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpPost;
/** CWE-522 basic auth over HTTP. Sinks: HttpPost(http url) and HttpURLConnection with an Authorization Basic header. */
public class CWE_522_InsecureBasicAuth extends HttpServlet {

    /** Test basic authentication with Apache HTTP request. */
    public void testApacheHttpRequest(String username, String password) {
        // BAD: basic authentication over HTTP
        String url = "http://www.example.com/rest/getuser.do?uid=abcdx";

        // GOOD: basic authentication over HTTPS
        url = "https://www.example.com/rest/getuser.do?uid=abcdx";

        HttpPost post = new HttpPost(url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");

        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        post.addHeader("Authorization", "Basic " + authStringEnc);
    }

    /** Test basic authentication with Java HTTP URL connection. */
    public void testHttpUrlConnection(String username, String password) throws Exception {
        // BAD: basic authentication over HTTP
        String urlStr = "http://www.example.com/rest/getuser.do?uid=abcdx";

        // GOOD: basic authentication over HTTPS
        urlStr = "https://www.example.com/rest/getuser.do?uid=abcdx";

        String authString = username + ":" + password;
        String encoding = Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic " + encoding);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        testApacheHttpRequest(username, password);
        try {
            testHttpUrlConnection(username, password);
        } catch (Exception e) { }
        response.getWriter().print("basic auth requests prepared");
    }
}
