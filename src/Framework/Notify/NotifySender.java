package Framework.Notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

public class NotifySender implements AutoCloseable {
    // Porta su cui inviare i pacchetti multicast
    private final int PORT;

    // Indirizzo del gruppo multicast
    private final InetAddress ADDRESS;

    // Dimensione massima del pacchetto che può essere inviato
    private final int MAX_PACKEGE_SIZE;

    // Socket multicast per inviare i pacchetti
    private final MulticastSocket multicastSocket;

    // Costruttore che configura il socket e le impostazioni base
    public NotifySender(int port, InetAddress address) throws IOException, SecurityException {
        this.PORT = port;
        this.ADDRESS = address;

        // Crea un socket multicast senza essere legato a una porta specifica
        this.multicastSocket = new MulticastSocket();

        // Ottiene la dimensione massima del buffer di invio del socket
        this.MAX_PACKEGE_SIZE = this.multicastSocket.getSendBufferSize();
    }

    // Metodo per inviare una notifica (messaggio) al gruppo multicast
    public void sendNotify(String message) throws DataTooLongException, IOException {
        // Converte il messaggio in un array di byte
        byte[] msg = message.getBytes();

        // Verifica che il messaggio non superi la dimensione massima consentita
        if (msg.length > MAX_PACKEGE_SIZE)
            throw new DataTooLongException("Message too long");

        // Crea un buffer con i primi 4 byte per la lunghezza del messaggio e il
        // messaggio stesso
        ByteBuffer buffer = ByteBuffer.allocate(4 + msg.length);

        // Inserisce la lunghezza del messaggio come intero nei primi 4 byte
        buffer.putInt(msg.length);

        // Inserisce il messaggio dopo i primi 4 byte
        buffer.put(msg);

        // Prepara il buffer per la lettura
        buffer.flip();

        // Crea un pacchetto Datagram da inviare all'indirizzo e alla porta specificati
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.capacity(), ADDRESS, PORT);

        // Invia il pacchetto al gruppo multicast
        this.multicastSocket.send(packet);
    }

    // Metodo per chiudere il socket multicast (implementa AutoCloseable)
    public void close() {
        this.multicastSocket.close();
    }
}
