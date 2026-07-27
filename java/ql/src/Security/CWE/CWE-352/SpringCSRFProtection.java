package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/** CWE-352 CSRF protection disabled. Sink: http.csrf(csrf -> csrf.disable()). */
public class CWE_352_SpringCSRFProtection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ObjectPostProcessor<Object> opp = new ObjectPostProcessor<Object>() {
                public <T> T postProcess(T o) { return o; }
            };
            AuthenticationManagerBuilder amb = new AuthenticationManagerBuilder(opp);
            HttpSecurity http = new HttpSecurity(opp, amb, new HashMap<>());
            // BAD - CSRF protection shouldn't be disabled
            http.csrf(csrf -> csrf.disable());
            response.getWriter().print("csrf disabled");
        } catch (Exception e) {
            response.getWriter().print("csrf config reached");
        }
    }
}
