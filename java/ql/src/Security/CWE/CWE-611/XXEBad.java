package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * CWE-611: XML External Entity (XXE) injection.
 * Source: the request body (request.getInputStream()). Sink: builder.parse(...).
 * Root cause: a default DocumentBuilder parses untrusted XML with DTD/entity processing enabled.
 */
public class CWE_611_XXEBad extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(request.getInputStream()); // BAD: DTD parsing is enabled
            response.getWriter().print("parsed");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

