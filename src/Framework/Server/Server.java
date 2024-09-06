package Framework.Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Notify.NotifySender;

public class Server implements AutoCloseable {
    // Variabili di configurazione del server
    public int PORT; // Porta su cui il server accetta connessioni
    public int THREAD_NUM; // Numero di thread nel pool
    public String DATA_DIR; // Directory dove sono salvati i dati del database
    public int NOTIFY_PORT; // Porta per le notifiche
    public String NOTIFY_ADDRESS; // Indirizzo per le notifiche multicast
    public int DATA_SAVE_TIMEOUT; // Timeout per il salvataggio dei dati
    public int NOTIFY_TIMEOUT; // Timeout per le notifiche

    // Componenti interne del server
    private ServerSocket server; // ServerSocket per accettare le connessioni
    private ExecutorService pool; // Pool di thread per gestire le richieste
    private NotifySender sender; // Componente per inviare notifiche multicast
    private File file; // File delle impostazioni del server

    // Costruttore di default: carica le impostazioni dal file di configurazione
    // "config_server.properties"
    public Server()
            throws IOException, SecurityException, IllegalArgumentException, DatabaseInizializeException {
        this("config_server.properties");
    }

    // Costruttore che consente di specificare un file di configurazione
    public Server(String settingFileName)
            throws IOException, SecurityException, IllegalArgumentException, DatabaseInizializeException {
        this.file = new File(settingFileName);
        this.loadData(); // Carica le impostazioni dal file

        // Inizializza il server socket, il pool di thread e il sender delle notifiche
        this.server = new ServerSocket(this.PORT);
        this.pool = Executors.newFixedThreadPool(this.THREAD_NUM);
        this.sender = new NotifySender(this.NOTIFY_PORT, InetAddress.getByName(this.NOTIFY_ADDRESS));

        // Inizializza il database e altre componenti
        Database.setDataPath(this.DATA_DIR);
        Database.addTableWithoutFile(Session.class); // Aggiunge la tabella Session al database
        Database.inizialize();
        Router.inizialize(); // Inizializza il router per le richieste
    }

    // Metodo privato per caricare i dati dal file di configurazione
    private void loadData() throws IOException {
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            // Assegna i valori alle variabili di configurazione del server
            this.PORT = Integer.parseInt(properties.getProperty("server_port"));
            this.THREAD_NUM = Integer.parseInt(properties.getProperty("thread_num"));
            this.DATA_DIR = properties.getProperty("data_dir");
            this.NOTIFY_ADDRESS = properties.getProperty("notify_address");
            this.NOTIFY_PORT = Integer.parseInt(properties.getProperty("notify_port"));
            this.DATA_SAVE_TIMEOUT = Integer.parseInt(properties.getProperty("data_save_timeout"));
            this.NOTIFY_TIMEOUT = Integer.parseInt(properties.getProperty("notify_timeout"));
        }
    }

    // Metodo per avviare il server e gestire le connessioni in arrivo
    public void run()
            throws IOException, SecurityException, RejectedExecutionException, NullPointerException {
        System.out.println("[SERVER] Running server on " + PORT);

        // Accetta continuamente connessioni finché il thread non viene interrotto
        while (!Thread.interrupted()) {
            Socket connection = this.server.accept(); // Attende una connessione
            System.out.println("Connection opened with: " + connection.getInetAddress());

            // Esegue il gestore della richiesta in un thread separato dal pool
            this.pool.execute(new RequestHandler(connection));
        }
    }

    // Restituisce l'istanza del NotifySender per inviare notifiche
    public NotifySender getNotifySender() {
        return this.sender;
    }

    // Metodo per chiudere correttamente il server e le sue risorse
    public void close() throws SecurityException, IOException {
        // Chiude il sender di notifiche e il server socket
        this.sender.close();
        this.server.close();
        this.pool.shutdown(); // Avvia lo shutdown del pool di thread

        try {
            // Attende la terminazione dei thread fino a 10 secondi
            if (!this.pool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Threads required too much time");
                this.pool.shutdownNow(); // Forza la terminazione dei thread
            }
        } catch (InterruptedException e) {
            this.pool.shutdownNow();
            Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione del thread
        }
    }
}
