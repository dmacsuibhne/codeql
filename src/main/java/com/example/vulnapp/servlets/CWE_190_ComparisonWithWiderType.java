package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-190: comparison with wider type causes short overflow / infinite loop.
 * The buggy loop is executed in a watchdog thread so the endpoint returns; the giant buffer of the
 * original snippet is reduced to avoid OutOfMemoryError (the buffer is incidental to the defect).
 */
public class CWE_190_ComparisonWithWiderType extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Thread t = new Thread(() -> {
            long MAXGET = Short.MAX_VALUE + 1;
            char[] buf = new char[1024];
            {
                short bytesReceived = 0;
                // BAD: 'bytesReceived' (short) compared with a wider type; overflows before reaching MAXGET
                while (bytesReceived < MAXGET) {
                    bytesReceived += getFromInput(buf, bytesReceived);
                }
            }
            {
                long bytesReceived2 = 0;
                // GOOD: 'bytesReceived2' has a type at least as wide as MAXGET.
                while (bytesReceived2 < MAXGET) {
                    bytesReceived2 += getFromInput(buf, bytesReceived2);
                }
            }
        });
        t.setDaemon(true);
        t.start();
        try { t.join(300); } catch (InterruptedException e) { }
        response.getWriter().print("loop entered=" + t.isAlive());
    }
    private static int getFromInput(char[] buf, long pos) {
        // write to buf
        // ...
        return 1;
    }
}
