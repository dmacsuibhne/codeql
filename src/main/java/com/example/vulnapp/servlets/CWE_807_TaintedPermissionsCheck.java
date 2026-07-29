package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
/** CWE-807 tainted permission check. Source: "action". Sink: subject.isPermitted("domain:sublevel:" + action). */
public class CWE_807_TaintedPermissionsCheck extends HttpServlet {
    static {
        SecurityUtils.setSecurityManager(new DefaultSecurityManager());
    }

    private static void doIt() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String whatDoTheyWantToDo = request.getParameter("action");
        Subject subject = SecurityUtils.getSubject();

        // BAD: permissions decision made using tainted data
        if (subject.isPermitted("domain:sublevel:" + whatDoTheyWantToDo))
            doIt();

        // GOOD: use fixed checks
        if (subject.isPermitted("domain:sublevel:whatTheMethodDoes"))
            doIt();

        response.getWriter().print("permission checks evaluated");
    }
}
