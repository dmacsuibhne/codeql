package com.example.vulnapp.servlets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-023 partial path traversal. Source: "path". Sink: getCanonicalPath().startsWith (not slash-terminated). */
public class CWE_023_PartialPathTraversalBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File parent = Files.createTempDirectory("parent").toFile();
        File dir = new File(parent, request.getParameter("path"));
        // BAD: dir.getCanonicalPath() not slash-terminated
        if (!dir.getCanonicalPath().startsWith(parent.getCanonicalPath())) {
            response.getWriter().print("rejected");
        } else {
            response.getWriter().print("accepted");
        }
    }
}
