import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

public class UnsafeDeserializationBad {

    static class MyObject implements Serializable {
        public int field;

        MyObject(int field) {
            this.field = field;
        }
    }

    public MyObject deserialize(Socket sock) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(sock.getInputStream())) {
            return (MyObject) in.readObject(); // BAD: in is from untrusted source
        }
    }
}
