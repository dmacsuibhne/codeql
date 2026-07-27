package com.example.vulnapp.servlets;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-421: data sent over an unauthenticated socket. Sink: connection1.getOutputStream().write(secretData). */
public class CWE_421_SocketAuthRace extends HttpServlet {
    private final byte[] secretData = "secret".getBytes();
    private boolean isAuthenticated(String username) { return true; }
    public void doConnect(ServerSocket listenSocket, String username) throws IOException {
        if (isAuthenticated(username)) {
            Socket connection1 = listenSocket.accept();
            // BAD: no authentication over the socket connection
            connection1.getOutputStream().write(secretData);
            connection1.close();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final ServerSocket listenSocket = new ServerSocket(0);
        int port = listenSocket.getLocalPort();
        Thread server = new Thread(() -> {
            try { doConnect(listenSocket, request.getParameter("username")); } catch (IOException e) { }
        });
        server.setDaemon(true);
        server.start();
        try (Socket client = new Socket("localhost", port)) {
            client.getInputStream().read();
        }
        listenSocket.close();
        response.getWriter().print("secret sent");
    }
}
