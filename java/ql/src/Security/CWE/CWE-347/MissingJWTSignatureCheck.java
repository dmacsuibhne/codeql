package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Jwts;
/** CWE-347 missing JWT signature check. Source: "token". Sink: parser.parse(token) (does not verify signature). */
public class CWE_347_MissingJWTSignatureCheck extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        try {
            // BAD: Does not verify the signature
            Jwts.parserBuilder().setSigningKey("someBase64EncodedKey").build().parse(token);

            // GOOD: Verify the signature
            Jwts.parserBuilder().setSigningKey("someBase64EncodedKey").build()
                    .parseClaimsJws(token)
                    .getBody();
            response.getWriter().print("parsed");
        } catch (Exception e) {
            response.getWriter().print("parse attempted");
        }
    }
}
