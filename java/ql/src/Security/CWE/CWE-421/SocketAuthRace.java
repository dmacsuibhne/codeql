import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAuthRace {
        private byte[] secretData;

        private boolean isAuthenticated(String username) {
                return true;
        }

        private boolean doAuthenticate(Socket connection, String username) {
                return true;
        }

public void doConnect(int desiredPort, String username) throws IOException {
        ServerSocket listenSocket = new ServerSocket(desiredPort);

        if (isAuthenticated(username)) {
                Socket connection1 = listenSocket.accept();
                // BAD: no authentication over the socket connection
                connection1.getOutputStream().write(secretData);
        }
}

public void doConnectGood(int desiredPort, String username) throws IOException {
        ServerSocket listenSocket = new ServerSocket(desiredPort);

        Socket connection2 = listenSocket.accept();
        // GOOD: authentication happens over the socket
        if (doAuthenticate(connection2, username)) {
                connection2.getOutputStream().write(secretData);
        }
}
}
