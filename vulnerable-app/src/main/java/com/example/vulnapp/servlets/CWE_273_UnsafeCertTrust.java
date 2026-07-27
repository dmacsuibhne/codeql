package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-273 RabbitMQ SSL without hostname verification. Sink: connectionFactory.useSslProtocol() (no enableHostnameVerification). */
public class CWE_273_UnsafeCertTrust extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
            // BAD: Hostname verification for rabbitmq ConnectionFactory is not enabled
            connectionFactory.useSslProtocol();
            response.getWriter().print("ssl configured");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
