package com.example.vulnapp.servlets;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * CWE-643: XPath Injection.
 * Source: request parameters "user" and "pass". Sink: xpath.evaluate(expression1, ...).
 * Root cause: untrusted input concatenated into an XPath expression.
 */
public class CWE_643_XPathInjection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String xmlStr = "<users>" +
                "   <user name=\"aaa\" pass=\"pass1\"></user>" +
                "   <user name=\"bbb\" pass=\"pass2\"></user>" +
                "</users>";
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            // Injectable data
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");
            if (user != null && pass != null) {
                // Bad expression
                String expression1 = "/users/user[@name='" + user + "' and @pass='" + pass + "']";
                boolean isExist = (boolean) xpath.evaluate(expression1, doc, XPathConstants.BOOLEAN);
                response.getWriter().print("exists=" + isExist);
            } else {
                response.getWriter().print("missing parameters");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

