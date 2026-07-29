package com.example.vulnapp.servlets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-022 (Zip Slip). Source: "entryName". Sink: new FileOutputStream(new File(dir, entry.getName())). */
public class CWE_022_ZipSlipBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ZipEntry entry = new ZipEntry(request.getParameter("entryName"));
        File destinationDir = Files.createTempDirectory("zipslip").toFile();
        File file = new File(destinationDir, entry.getName());
        FileOutputStream fos = new FileOutputStream(file); // BAD
        fos.close();
        response.getWriter().print("wrote " + file.getPath());
    }
}
