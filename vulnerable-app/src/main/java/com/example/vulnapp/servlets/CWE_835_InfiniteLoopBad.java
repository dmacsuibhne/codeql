package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-835: potential infinite loop (inner condition tests the wrong variable).
 * Executed in a watchdog thread so the endpoint returns.
 */
public class CWE_835_InfiniteLoopBad extends HttpServlet {
    private volatile boolean stop = false;
    private boolean shouldBreak() { return stop; }
    void run() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; i < 10; j++) { // BAD: potential infinite loop: i should be j
                // do stuff
                if (shouldBreak()) return;
            }
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Thread t = new Thread(this::run);
        t.setDaemon(true);
        t.start();
        try { t.join(300); } catch (InterruptedException e) { }
        stop = true;
        response.getWriter().print("loop entered=" + t.isAlive());
    }
}
