package Framework.Notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class NotifySender implements AutoCloseable {
    private final int PORT;
    private final InetAddress ADDRESS;
    private final int MAX_PACKEGE_SIZE;
    private final MulticastSocket multicastSocket;

    public NotifySender(int port, int maxPackegeSize, InetAddress address) throws IOException, SecurityException {
        this.PORT = port;
        this.MAX_PACKEGE_SIZE = maxPackegeSize;
        this.ADDRESS = address;
        this.multicastSocket = new MulticastSocket();
    }

    public void sendNotify(String message) throws DataTooLongException, IOException {
        byte[] msg = message.getBytes();
        if(msg.length > MAX_PACKEGE_SIZE) 
            throw new DataTooLongException("Message too long");

        ByteBuffer buffer = ByteBuffer.allocate(4 + msg.length);
        buffer.putInt(msg.length);
        buffer.put(msg);
        buffer.flip();

        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity(), ADDRESS, PORT);
        this.multicastSocket.send(packet);
    }

    public void close() {
        this.multicastSocket.close();
    }

}
