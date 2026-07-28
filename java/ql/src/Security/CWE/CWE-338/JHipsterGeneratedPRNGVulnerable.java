package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
/**
 * CWE-338: insecure PRNG for secrets. Sinks: RandomStringUtils.randomAlphanumeric/randomNumeric,
 * which do not use SecureRandom. Mirrors the five JHipster generator methods of the original snippet.
 */
public class CWE_338_JHipsterGeneratedPRNGVulnerable extends HttpServlet {

    private static final int DEF_COUNT = 20;

    /**
     * Generate a password.
     *
     * @return the generated password.
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT); // BAD: RandomStringUtils does not use SecureRandom
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key.
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT); // BAD: RandomStringUtils does not use SecureRandom
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key.
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT); // BAD: RandomStringUtils does not use SecureRandom
    }

    /**
     * Generate a unique series to validate a persistent token, used in the
     * authentication remember-me mechanism.
     *
     * @return the generated series data.
     */
    public static String generateSeriesData() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT); // BAD: RandomStringUtils does not use SecureRandom
    }

    /**
     * Generate a persistent token, used in the authentication remember-me mechanism.
     *
     * @return the generated token data.
     */
    public static String generateTokenData() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT); // BAD: RandomStringUtils does not use SecureRandom
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String password = generatePassword();
        generateActivationKey();
        generateResetKey();
        generateSeriesData();
        generateTokenData();
        response.getWriter().print(password);
    }
}
