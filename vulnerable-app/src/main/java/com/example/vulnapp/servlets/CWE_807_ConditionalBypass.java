package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-807: security decision based on a user-controlled cookie. Sink: if (adminCookie.getValue() == "false"). */
public class CWE_807_ConditionalBypass extends HttpServlet {
    private boolean login(String user, String password) { return true; }
    private boolean queryDbForAdminStatus(String user, String password) { return false; }
    public boolean doLogin(Cookie adminCookie, String user, String password) {
        // BAD: login executed based on a user-controlled cookie value
        if (adminCookie.getValue() == "false")
            return login(user, password);
        return true;
    }
    public boolean doLoginGood(String user, String password) {
        // GOOD: use server-side information based on the credentials to decide
        // whether user has privileges
        boolean isAdmin = queryDbForAdminStatus(user, password);
        if (!isAdmin)
            return login(user, password);
        return true;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie adminCookie = new Cookie("admin", request.getParameter("admin"));
        boolean result = doLogin(adminCookie, "user", "pass");
        response.getWriter().print("login=" + result);
    }
}
