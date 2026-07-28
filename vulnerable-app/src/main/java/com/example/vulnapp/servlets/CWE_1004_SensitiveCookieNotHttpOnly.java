package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.NewCookie;
/** CWE-1004: sensitive cookie without HttpOnly. Source: "jwt_token". Sink: response.addCookie(cookie). */
public class CWE_1004_SensitiveCookieNotHttpOnly extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jwt_token = request.getParameter("jwt_token");
        String authId = jwt_token;
        String accessKey = jwt_token;

        // GOOD - Create a sensitive cookie with the `HttpOnly` flag set.
        Cookie goodJwtCookie = new Cookie("jwt_token", jwt_token);
        goodJwtCookie.setPath("/");
        goodJwtCookie.setMaxAge(3600 * 24 * 7);
        goodJwtCookie.setHttpOnly(true);
        response.addCookie(goodJwtCookie);

        // BAD - Create a sensitive cookie without the `HttpOnly` flag set.
        Cookie jwtCookie = new Cookie("jwt_token", jwt_token);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600 * 24 * 7);
        response.addCookie(jwtCookie);

        // GOOD - Set a sensitive cookie header with the `HttpOnly` flag set.
        response.addHeader("Set-Cookie", "token=" + authId + ";HttpOnly;Secure");

        // BAD - Set a sensitive cookie header without the `HttpOnly` flag set.
        response.addHeader("Set-Cookie", "token=" + authId + ";Secure");

        // The javax.ws.rs.core.NewCookie variants below need a JAX-RS runtime (RuntimeDelegate) that
        // is not shipped here; they are retained to mirror the original snippet's BAD/GOOD sinks.
        try {
            // GOOD - Set a sensitive cookie header using `javax.ws.rs.core.Cookie` with the `HttpOnly` flag set through string concatenation.
            response.setHeader("Set-Cookie", new NewCookie("session-access-key", accessKey, "/", null, null, 0, true) + ";HttpOnly");

            // BAD - Set a sensitive cookie header using `javax.ws.rs.core.Cookie` without the `HttpOnly` flag set.
            response.setHeader("Set-Cookie", new NewCookie("session-access-key", accessKey, "/", null, null, 0, true).toString());

            // GOOD - Set a sensitive cookie header using `javax.ws.rs.core.Cookie` with the `HttpOnly` flag set through the constructor.
            NewCookie accessKeyCookie = new NewCookie("session-access-key", accessKey, "/", null, null, 0, true, true);
            response.setHeader("Set-Cookie", accessKeyCookie.toString());
        } catch (Throwable jaxrsRuntimeMissing) {
            // RuntimeDelegate implementation not available in this environment.
        }

        response.getWriter().print("cookie set");
    }
}
