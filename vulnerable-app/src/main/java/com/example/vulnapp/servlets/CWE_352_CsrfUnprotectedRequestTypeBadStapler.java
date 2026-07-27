package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.verb.GET;
/** CWE-352 Stapler state-changing action exposed over GET. Sink: @GET doTransfer(). */
public class CWE_352_CsrfUnprotectedRequestTypeBadStapler extends HttpServlet {
    static class BadStapler {
        HttpRedirect transfer() { return new HttpRedirect("done"); }
        // BAD - a safe HTTP request like GET should not be used for a state-changing action
        @GET
        public HttpRedirect doTransfer() { return transfer(); }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpRedirect redirect = new BadStapler().doTransfer();
        response.getWriter().print("transfer invoked=" + (redirect != null));
    }
}
