package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-078: unescaped command string. Source: "latlonCoords". Sink: rt.exec("cmd.exe /C latlon2utm.exe " + coords). */
public class CWE_078_ExecUnescaped extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String latlonCoords = request.getParameter("latlonCoords");
        try {
            // BAD: user input might include special characters such as ampersands
            {
                Runtime rt = Runtime.getRuntime();
                Process exec = rt.exec("cmd.exe /C latlon2utm.exe " + latlonCoords);
            }

            // GOOD: use an array of arguments instead of executing a string
            {
                Runtime rt = Runtime.getRuntime();
                Process exec = rt.exec(new String[]{
                        "c:\\path\\to\\latlon2utm.exe",
                        latlonCoords});
            }
        } catch (Exception e) {
            // cmd.exe absent on non-Windows; the sink has still been reached
        }
        response.getWriter().print("exec unescaped dispatched");
    }
}
