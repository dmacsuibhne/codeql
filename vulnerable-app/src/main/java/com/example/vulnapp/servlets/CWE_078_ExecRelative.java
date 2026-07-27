package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-078: relative-path command execution. Sink: Runtime.getRuntime().exec("make"). */
public class CWE_078_ExecRelative extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // BAD: relative path
            Runtime.getRuntime().exec("make");
        } catch (Exception e) {
            // command may be absent; the sink has still been reached
        }
        response.getWriter().print("exec relative dispatched");
    }
}
