package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
/** CWE-798 hardcoded AWS credentials. Sink: new BasicAWSCredentials("ACCESS_KEY", "SECRET_KEY"). */
public class CWE_798_HardcodedAWSCredentials extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: Hardcoded credentials for connecting to AWS services
        AWSCredentials creds = new BasicAWSCredentials("ACCESS_KEY", "SECRET_KEY");
        response.getWriter().print("access=" + creds.getAWSAccessKeyId());
    }
}
