package com.example.vulnapp.servlets;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-319: RMI export without SSL socket factories. Sink: UnicastRemoteObject.exportObject(obj, 0). */
public class CWE_319_UseSSLSocketFactories extends HttpServlet {
    interface Test extends Remote { }
    static class TestImpl implements Test { }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            TestImpl obj = new TestImpl();
            // BAD: default socket factory is used
            Test stub = (Test) UnicastRemoteObject.exportObject(obj, 0);
            UnicastRemoteObject.unexportObject(obj, true);
            response.getWriter().print("exported");
        } catch (Exception e) {
            response.getWriter().print("export attempted");
        }
    }
}
