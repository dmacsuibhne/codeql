package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/**
 * CWE-200 unauthenticated access to Spring Boot actuator endpoints.
 * Sink: authorizeHttpRequests(... permitAll()) applied to EndpointRequest.toAnyEndpoint().
 * (Uses requestMatcher, the Spring Security 5.7 equivalent of the snippet's securityMatcher.)
 */
public class CWE_200_SpringBootActuators extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ObjectPostProcessor<Object> opp = new ObjectPostProcessor<Object>() {
                public <T> T postProcess(T o) { return o; }
            };
            AuthenticationManagerBuilder amb = new AuthenticationManagerBuilder(opp);
            HttpSecurity http = new HttpSecurity(opp, amb, new HashMap<>());
            http.requestMatcher(EndpointRequest.toAnyEndpoint());
            // BAD: Unauthenticated access to Spring Boot actuator endpoints is allowed
            http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());

            // GOOD: only users with ENDPOINT_ADMIN role are allowed to access the actuator endpoints
            http.authorizeHttpRequests((requests) -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
            response.getWriter().print("actuators permitAll");
        } catch (Exception e) {
            response.getWriter().print("actuator config reached");
        }
    }
}
