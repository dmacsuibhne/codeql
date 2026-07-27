package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-209: stack trace exposure. Sink: ex.printStackTrace(response.getWriter()). */
public class CWE_209_StackTraceExposure extends HttpServlet {
    private void doSomeWork() {
        throw new NullPointerException("boom");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doSomeWork();
        } catch (NullPointerException ex) {
            // BAD: printing a stack trace back to the response
            ex.printStackTrace(response.getWriter());
        }
    }
}
