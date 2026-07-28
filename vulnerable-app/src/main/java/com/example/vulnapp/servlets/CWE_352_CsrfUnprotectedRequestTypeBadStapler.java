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
        HttpRedirect post() { return new HttpRedirect("done"); }
        // BAD - a safe HTTP request like GET should not be used for a state-changing action
        @GET
        public HttpRedirect doTransfer() { return transfer(); }
        // BAD - no HTTP request type is specified, so safe HTTP requests are allowed
        public HttpRedirect doPost() { return post(); }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BadStapler bad = new BadStapler();
        HttpRedirect redirect = bad.doTransfer();
        bad.doPost();
        response.getWriter().print("transfer invoked=" + (redirect != null));
    }
}
