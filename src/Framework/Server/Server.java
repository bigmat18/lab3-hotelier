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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Notify.NotifySender;

public class Server implements AutoCloseable {
    public int PORT;
    public int THREAD_NUM;
    public String DATA_DIR;
    public int NOTIFY_PORT;
    public String NOTIFY_ADDRESS;
    public int DATA_SAVE_TIMEOUT;
    public int NOTIFY_TIMEOUT;

    private ServerSocket server;
    private ExecutorService pool;
    private NotifySender sender;
    private File file;

    public Server()
            throws IOException, SecurityException, IllegalArgumentException, DatabaseInizializeException
    {
        this("config_server.properties");
    }

    public Server(String settingFileName)
        throws IOException, SecurityException, IllegalArgumentException, DatabaseInizializeException
    {
        this.file = new File(settingFileName);
        this.loadData();

        this.server = new ServerSocket(this.PORT);
        this.pool = Executors.newFixedThreadPool(this.THREAD_NUM);
        this.sender = new NotifySender(this.NOTIFY_PORT, InetAddress.getByName(this.NOTIFY_ADDRESS));

        Database.setDataPath(this.DATA_DIR);
        Database.addTableWithoutFile(Session.class);
        Database.inizialize();
        Router.inizialize();
    }

    private void loadData() throws IOException {
        
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            this.PORT = Integer.parseInt(properties.getProperty("server_port"));
            this.THREAD_NUM = Integer.parseInt(properties.getProperty("thread_num"));
            this.DATA_DIR = properties.getProperty("data_dir");
            this.NOTIFY_ADDRESS = properties.getProperty("notify_address");
            this.NOTIFY_PORT = Integer.parseInt(properties.getProperty("notify_port"));
            this.DATA_SAVE_TIMEOUT = Integer.parseInt(properties.getProperty("data_save_timeout"));
            this.NOTIFY_TIMEOUT = Integer.parseInt(properties.getProperty("notify_timeout"));
        }
    }

    public void run()
            throws IOException, SecurityException, RejectedExecutionException, NullPointerException
    {
        System.out.println("[SERVER] Running server on " + PORT);
        while (!Thread.interrupted()) {
            Socket connection = this.server.accept();
            System.out.println("Connection opened with: " + connection.getInetAddress());
            this.pool.execute(new RequestHandler(connection));
        }
    }

    public NotifySender getNotifySender() { return this.sender; }

    public void close() throws SecurityException, IOException {
        this.sender.close();
        this.server.close();
        this.pool.shutdown();
    
        try {
            if (!this.pool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Threads required too much time");
                this.pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
