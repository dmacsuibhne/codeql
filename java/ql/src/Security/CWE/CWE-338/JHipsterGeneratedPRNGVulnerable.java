package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
/** CWE-338: insecure PRNG for secrets. Sink: RandomStringUtils.randomAlphanumeric(...). */
public class CWE_338_JHipsterGeneratedPRNGVulnerable extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: RandomStringUtils does not use SecureRandom
        String password = RandomStringUtils.randomAlphanumeric(20);
        response.getWriter().print(password);
    }
}
