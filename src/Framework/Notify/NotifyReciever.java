package Framework.Notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class NotifyReciever implements AutoCloseable {
    private final InetAddress ADDRESS;
    private final int PORT;
    private byte[] buffer;
    private MulticastSocket socket;

    public NotifyReciever(int port, InetAddress address, int timeout) 
        throws IOException, SecurityException, IllegalArgumentException 
    {
        this.ADDRESS = address;
        this.PORT = port;
        this.socket = new MulticastSocket(this.PORT);
        this.socket.setSoTimeout(timeout);
        this.buffer = new byte[this.socket.getReceiveBufferSize()];
        this.socket.joinGroup(address);
    }

    public byte[] receiveNotify() throws DataTooLongException, IOException {
        DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);
        socket.receive(packet);

        ByteBuffer sizeBuffer = ByteBuffer.wrap(packet.getData());  
        int size = sizeBuffer.getInt();

        if (packet.getLength() != size + 4)
            throw new DataTooLongException("Message too long");

        byte[] data = new byte[size];
        System.arraycopy(packet.getData(), 4, data, 0, size);
        return data;
    }

    public void close() {
        this.socket.close();
    }
}
