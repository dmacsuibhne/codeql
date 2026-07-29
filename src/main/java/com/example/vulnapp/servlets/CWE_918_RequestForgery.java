package com.example.vulnapp.servlets;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-918: Server-Side Request Forgery.
 * Source: request parameter "uri". Sink: client.send(HttpRequest built from uri).
 * Root cause: user input used to build an outbound HTTP request without validation.
 */
public class CWE_918_RequestForgery extends HttpServlet {
    private static final String VALID_URI = "http://lgtm.com";
    private final HttpClient client = HttpClient.newHttpClient();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            URI uri = new URI(request.getParameter("uri"));
            // BAD: a request parameter is incorporated without validation into a Http request
            HttpRequest r = HttpRequest.newBuilder(uri).build();
            client.send(r, HttpResponse.BodyHandlers.discarding());

            // GOOD: the request parameter is validated against a known fixed string
            if (VALID_URI.equals(request.getParameter("uri"))) {
                HttpRequest r2 = HttpRequest.newBuilder(uri).build();
                client.send(r2, HttpResponse.BodyHandlers.discarding());
            }
            response.getWriter().print("sent");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

