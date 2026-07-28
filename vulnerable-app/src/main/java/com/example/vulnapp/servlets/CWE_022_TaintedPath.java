package com.example.vulnapp.servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-022: Path Traversal.
 * Source: request parameter "filename". Sink: new FileReader(filename).
 * Root cause: a file is read from a user-supplied path without validation.
 */
public class CWE_022_TaintedPath extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filename = request.getParameter("filename");
        // BAD: read from a file without checking its path
        BufferedReader fileReader = new BufferedReader(new FileReader(filename));
        String fileLine = fileReader.readLine();
        while (fileLine != null) {
            response.getOutputStream().write(fileLine.getBytes());
            fileLine = fileReader.readLine();
        }
    }
}

