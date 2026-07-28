package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-273 missing TLS hostname verification, shown across three APIs (SSLEngine, SSLSocket and
 * RabbitMQ ConnectionFactory) exactly as in the original snippet.
 * The JSSE blocks are individually guarded only because {@code createSSLEngine()} on an
 * uninitialized {@code SSLContext} and {@code createSocket(host, 443)} (a real outbound TLS
 * connection) would otherwise fail the endpoint; the vulnerable/safe calls are unchanged.
 */
public class CWE_273_UnsafeCertTrust extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            SSLEngine sslEngine = sslContext.createSSLEngine();
            SSLParameters sslParameters = sslEngine.getSSLParameters();
            sslParameters.setEndpointIdentificationAlgorithm("HTTPS"); //GOOD: Set a valid endpointIdentificationAlgorithm for SSL engine to trigger hostname verification
            sslEngine.setSSLParameters(sslParameters);
        } catch (Exception e) { }

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            SSLEngine sslEngine = sslContext.createSSLEngine();  //BAD: No endpointIdentificationAlgorithm set
        } catch (Exception e) { }

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            final SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("www.example.com", 443);
            SSLParameters sslParameters = socket.getSSLParameters();
            sslParameters.setEndpointIdentificationAlgorithm("HTTPS"); //GOOD: Set a valid endpointIdentificationAlgorithm for SSL socket to trigger hostname verification
            socket.setSSLParameters(sslParameters);
        } catch (Exception e) { }

        try {
            com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
            connectionFactory.useSslProtocol();
            connectionFactory.enableHostnameVerification();  //GOOD: Enable hostname verification for rabbitmq ConnectionFactory
        } catch (Exception e) { }

        try {
            com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
            connectionFactory.useSslProtocol(); //BAD: Hostname verification for rabbitmq ConnectionFactory is not enabled
        } catch (Exception e) { }

        response.getWriter().print("ssl configured");
    }
}
