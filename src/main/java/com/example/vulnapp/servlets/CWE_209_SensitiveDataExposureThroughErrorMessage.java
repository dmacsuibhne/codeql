package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-209: sensitive data in error message. Sink: response.sendError(500, ex.getMessage()). */
public class CWE_209_SensitiveDataExposureThroughErrorMessage extends HttpServlet {
    private void doSomeWork(String detail) {
        throw new NullPointerException("internal failure near " + detail);
    }
    private void log(String message, String detail) {
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doSomeWork(request.getParameter("detail"));
        } catch (NullPointerException ex) {
            // BAD: printing an exception message back to the response
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            return;
        }

        try {
            doSomeWork(request.getParameter("detail"));
        } catch (NullPointerException ex) {
            // GOOD: log the exception message, and send back a non-revealing response
            log("Exception occurred", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Exception occurred");
            return;
        }
    }
}
