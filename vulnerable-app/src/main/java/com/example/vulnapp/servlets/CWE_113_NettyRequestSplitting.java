package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
/** CWE-113 Netty request splitting. Sink: new DefaultHttpRequest(..., false) disables CRLF validation. */
public class CWE_113_NettyRequestSplitting extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getParameter("uri");
        // BAD: Disables the internal request splitting verification
        DefaultHttpHeaders badHeaders = new DefaultHttpHeaders(false);
        DefaultHttpRequest badRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri, false);

        // GOOD: Verifies headers passed don't contain CRLF characters
        DefaultHttpHeaders goodHeaders = new DefaultHttpHeaders();
        DefaultHttpRequest goodRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri);

        response.getWriter().print("built " + badRequest.method() + " headers=" + badHeaders.size());
    }
}
