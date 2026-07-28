package com.example.vulnapp.servlets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-200 local information disclosure via world-readable temp files/directories.
 * Sinks: File.createTempFile / Files.createTempDir / File.mkdir / Files.createFile /
 * Files.createDirectory, all creating entries with default (group/other-readable) permissions.
 * Each sink is individually guarded only so that fixed-name collisions on repeated HTTP requests
 * do not fail the endpoint; the vulnerable calls themselves are unchanged from the original snippet.
 */
public class CWE_200_TempDirUsageVulnerable extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            File temp1 = File.createTempFile("random", ".txt"); // BAD: File has permissions `-rw-r--r--`
        } catch (Exception e) { }

        try {
            File temp2 = File.createTempFile("random", "file", null); // BAD: File has permissions `-rw-r--r--`
        } catch (Exception e) { }

        try {
            File systemTempDir = new File(System.getProperty("java.io.tmpdir"));
            File temp3 = File.createTempFile("random", "file", systemTempDir); // BAD: File has permissions `-rw-r--r--`
        } catch (Exception e) { }

        try {
            File tempDir = com.google.common.io.Files.createTempDir(); // BAD: CVE-2020-8908: Directory has permissions `drwxr-xr-x`
        } catch (Exception e) { }

        try {
            new File(System.getProperty("java.io.tmpdir"), "/child").mkdir(); // BAD: Directory has permissions `-rw-r--r--`
        } catch (Exception e) { }

        try {
            File tempDirChildFile = new File(System.getProperty("java.io.tmpdir"), "/child-create-file.txt");
            Files.createFile(tempDirChildFile.toPath()); // BAD: File has permissions `-rw-r--r--`
        } catch (Exception e) { }

        try {
            File tempDirChildDir = new File(System.getProperty("java.io.tmpdir"), "/child-dir");
            tempDirChildDir.mkdir(); // BAD: Directory has permissions `drwxr-xr-x`
            Files.createDirectory(tempDirChildDir.toPath()); // BAD: Directory has permissions `drwxr-xr-x`
        } catch (Exception e) { }

        response.getWriter().print("temp entries created");
    }
}
