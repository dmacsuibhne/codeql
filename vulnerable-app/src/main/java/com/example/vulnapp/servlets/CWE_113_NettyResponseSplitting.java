package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
/** CWE-113 Netty response splitting. Sink: new DefaultHttpResponse(..., false) disables CRLF validation. */
public class CWE_113_NettyResponseSplitting extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: Disables the internal response splitting verification
        DefaultHttpHeaders badHeaders = new DefaultHttpHeaders(false);
        DefaultHttpResponse badResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, false);

        // GOOD: Verifies headers passed don't contain CRLF characters
        DefaultHttpHeaders goodHeaders = new DefaultHttpHeaders();
        DefaultHttpResponse goodResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        response.getWriter().print("built status=" + badResponse.status().code() + " headers=" + badHeaders.size());
    }
}
