package com.example.vulnapp.servlets;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
/** CWE-074 XSLT injection. Source: "xslt" param. Sink: factory.newTransformer(xslt).transform(...). */
public class CWE_074_XsltInjection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            StreamSource xslt = new StreamSource(new StringReader(request.getParameter("xslt")));
            StreamSource xml = new StreamSource(new StringReader("<a/>"));
            StringWriter result = new StringWriter();
            TransformerFactory factory = TransformerFactory.newInstance();
            // BAD: User provided XSLT stylesheet is processed
            factory.newTransformer(xslt).transform(xml, new StreamResult(result));
            response.getWriter().print("transformed");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
