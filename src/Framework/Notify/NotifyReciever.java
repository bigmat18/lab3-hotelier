package Framework.Notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class NotifyReciever implements AutoCloseable {
    // Indirizzo e porta del gruppo multicast
    private final InetAddress ADDRESS;
    private final int PORT;

    // Buffer per ricevere i dati
    private byte[] buffer;

    // Socket per ricevere i pacchetti multicast
    private MulticastSocket socket;

    // Costruttore per creare un ricevitore di notifiche
    public NotifyReciever(int port, InetAddress address, int timeout)
            throws IOException, SecurityException, IllegalArgumentException {
        this.ADDRESS = address;
        this.PORT = port;

        // Crea un socket multicast sulla porta specificata
        this.socket = new MulticastSocket(this.PORT);

        // Imposta il timeout per le operazioni di ricezione
        this.socket.setSoTimeout(timeout);

        // Inizializza il buffer con la dimensione massima di ricezione del socket
        this.buffer = new byte[this.socket.getReceiveBufferSize()];

        // Unisce il socket al gruppo multicast
        this.socket.joinGroup(address);
    }

    // Metodo per ricevere notifiche (dati) dal gruppo multicast
    public byte[] receiveNotify() throws DataTooLongException, IOException {
        // Crea un DatagramPacket per ricevere i dati
        DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);

        // Riceve un pacchetto dal gruppo multicast
        socket.receive(packet);

        // Legge la dimensione dei dati ricevuti (i primi 4 byte contengono la
        // dimensione)
        ByteBuffer sizeBuffer = ByteBuffer.wrap(packet.getData());
        int size = sizeBuffer.getInt();

        // Controlla se la lunghezza del pacchetto è corretta (dimensione dati + 4 byte
        // per la dimensione)
        if (packet.getLength() != size + 4)
            throw new DataTooLongException("Message too long");

        // Estrae i dati dal pacchetto a partire dal 5° byte (i primi 4 byte sono per la
        // dimensione)
        byte[] data = new byte[size];
        System.arraycopy(packet.getData(), 4, data, 0, size);

        // Restituisce i dati ricevuti
        return data;
    }

    // Metodo per unirsi al gruppo multicast (di nuovo, nel caso sia necessario)
    public void joinGroup() throws IOException {
        this.socket.joinGroup(this.ADDRESS);
    }

    // Metodo per lasciare il gruppo multicast
    public void leaveGroup() throws IOException {
        this.socket.leaveGroup(this.ADDRESS);
    }

    // Metodo per chiudere il socket (implementa AutoCloseable)
    public void close() {
        this.socket.close();
    }
}
