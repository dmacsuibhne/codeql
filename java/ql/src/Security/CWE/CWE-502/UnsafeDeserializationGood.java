import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class UnsafeDeserializationGood {

    static class MyObject {
        public int field;

        MyObject(int field) {
            this.field = field;
        }
    }

    public MyObject deserialize(Socket sock) throws IOException {
        try (DataInputStream in = new DataInputStream(sock.getInputStream())) {
            return new MyObject(in.readInt()); // GOOD: read only an int
        }
    }
}
