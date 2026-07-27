package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/** CWE-352 state-changing action reachable via GET. Sink: @RequestMapping(GET) doTransfer invoked on a GET request. */
public class CWE_352_CsrfUnprotectedRequestTypeBadSpring extends HttpServlet {
    static class BadController {
        boolean transfer(HttpServletRequest request, HttpServletResponse response) { return true; }
        // BAD - a safe HTTP request like GET should not be used for a state-changing action
        @RequestMapping(value = "/transfer", method = RequestMethod.GET)
        public boolean doTransfer(HttpServletRequest request, HttpServletResponse response) {
            return transfer(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean result = new BadController().doTransfer(request, response);
        response.getWriter().print("transfer=" + result);
    }
}
