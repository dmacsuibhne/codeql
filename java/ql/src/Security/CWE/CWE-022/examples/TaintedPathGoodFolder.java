import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaintedPathGoodFolder {
    public void sendUserFileGood(Socket sock, String user) throws IOException {
        BufferedReader filenameReader = new BufferedReader(
                new InputStreamReader(sock.getInputStream(), "UTF-8"));
        String filename = filenameReader.readLine();

        Path publicFolder = Paths.get("/home/" + user + "/public").normalize().toAbsolutePath();
        Path filePath = publicFolder.resolve(filename).normalize().toAbsolutePath();

        // GOOD: ensure that the path stays within the public folder
        if (!filePath.startsWith(publicFolder + File.separator)) {
            throw new IllegalArgumentException("Invalid filename");
        }
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath.toString()));
        String fileLine = fileReader.readLine();
        while (fileLine != null) {
            sock.getOutputStream().write(fileLine.getBytes());
            fileLine = fileReader.readLine();
        }
    }
}
