package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
/**
 * CWE-200 unauthenticated access to Spring Boot actuator endpoints.
 * The vulnerability is a Spring Security configuration: the BAD {@link SecurityFilterChain} bean
 * applies {@code permitAll()} to {@code EndpointRequest.toAnyEndpoint()}. The two mutually-exclusive
 * {@code @Configuration} classes below mirror the original snippet (you would use one OR the other);
 * the GOOD variant restricts access to the {@code ENDPOINT_ADMIN} role.
 * (Uses {@code requestMatcher}, the Spring Security 5.7 equivalent of the snippet's 5.8
 * {@code securityMatcher}.)
 */
public class CWE_200_SpringBootActuators extends HttpServlet {

    @Configuration(proxyBeanMethods = false)
    public static class CustomSecurityConfigurationBad {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // BAD: Unauthenticated access to Spring Boot actuator endpoints is allowed
            http.requestMatcher(EndpointRequest.toAnyEndpoint());
            http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
            return http.build();
        }

    }

    @Configuration(proxyBeanMethods = false)
    public static class CustomSecurityConfigurationGood {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // GOOD: only users with ENDPOINT_ADMIN role are allowed to access the actuator endpoints
            http.requestMatcher(EndpointRequest.toAnyEndpoint());
            http.authorizeHttpRequests((requests) -> requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
            return http.build();
        }

    }

    private static HttpSecurity newHttpSecurity() {
        ObjectPostProcessor<Object> opp = new ObjectPostProcessor<Object>() {
            public <T> T postProcess(T o) { return o; }
        };
        AuthenticationManagerBuilder amb = new AuthenticationManagerBuilder(opp);
        return new HttpSecurity(opp, amb, new HashMap<>());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Exercise the misconfigured (BAD) SecurityFilterChain bean so the permitAll() sink runs.
            new CustomSecurityConfigurationBad().securityFilterChain(newHttpSecurity());
            response.getWriter().print("actuators permitAll");
        } catch (Exception e) {
            response.getWriter().print("actuator config reached");
        }
    }
}
