package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-367: time-of-check to time-of-use race. Sink: r.act() guarded by a stale r.isReady() check. */
public class CWE_367_TOCTOURace extends HttpServlet {
    static class Resource {
        private boolean ready = true;
        public synchronized boolean isReady() { return ready; }
        public synchronized void act() {
            if (!isReady()) throw new IllegalStateException();
        }
    }
    public synchronized void bad(Resource r) {
        if (r.isReady()) {
            // BAD: r might no longer be ready by the time act() runs
            r.act();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        bad(new Resource());
        response.getWriter().print("acted");
    }
}
