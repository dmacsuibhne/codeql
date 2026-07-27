package com.example.vulnapp.servlets;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-200 local information disclosure via world-readable temp files. Sink: File.createTempFile / Files.createTempDir. */
public class CWE_200_TempDirUsageVulnerable extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File temp1 = File.createTempFile("random", ".txt"); // BAD: permissions -rw-r--r--
        File tempDir = com.google.common.io.Files.createTempDir(); // BAD: CVE-2020-8908
        response.getWriter().print("created " + temp1.getName() + " and " + tempDir.getName());
    }
}
