package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtHandlerAdapter;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
/** CWE-347 missing JWT signature check. Source: "token". Sink: parser.parse(token) (does not verify signature). */
public class CWE_347_MissingJWTSignatureCheck extends HttpServlet {

    public void badJwt(String token) {
        Jwts.parserBuilder()
                .setSigningKey("someBase64EncodedKey").build()
                .parse(token); // BAD: Does not verify the signature
    }

    public void badJwtHandler(String token) {
        Jwts.parserBuilder()
                .setSigningKey("someBase64EncodedKey").build()
                .parse(token, new JwtHandlerAdapter<Jwt<Header, String>>() {
                    @Override
                    public Jwt<Header, String> onPlaintextJwt(Jwt<Header, String> jwt) {
                        return jwt;
                    }
                }); // BAD: The handler is called on an unverified JWT
    }

    public void goodJwt(String token) {
        Jwts.parserBuilder()
                .setSigningKey("someBase64EncodedKey").build()
                .parseClaimsJws(token) // GOOD: Verify the signature
                .getBody();
    }

    public void goodJwtHandler(String token) {
        Jwts.parserBuilder()
                .setSigningKey("someBase64EncodedKey").build()
                .parse(token, new JwtHandlerAdapter<Jws<String>>() {
                    @Override
                    public Jws<String> onPlaintextJws(Jws<String> jws) {
                        return jws;
                    }
                }); // GOOD: The handler is called on a verified JWS
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        try {
            badJwt(token);
            badJwtHandler(token);
            goodJwt(token);
            goodJwtHandler(token);
            response.getWriter().print("parsed");
        } catch (Exception e) {
            response.getWriter().print("parse attempted");
        }
    }
}
