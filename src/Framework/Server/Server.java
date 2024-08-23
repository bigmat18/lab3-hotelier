package Framework.Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Data.Hotel;
import Data.Review;
import Data.User;
import Framework.Database.Database;
import Framework.Notify.NotifySender;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements AutoCloseable {
    public int PORT;
    public int THREAD_NUM;
    public String DATA_DIR;
    public int NOTIFY_PORT;
    public String NOTIFY_ADDRESS;
    public int DATA_SAVE_TIMEOUT;
    public int NOTIFY_UPDATE_TIMEOUT;

    private ServerSocket server;
    private ExecutorService pool;
    private NotifySender sender;
    private File file;
    private ArrayList<Timer> tasks;

    public Server()
            throws IOException, SecurityException, IllegalArgumentException 
    {
        this("config.json");
    }

    public Server(String settingFileName)
        throws IOException, SecurityException, IllegalArgumentException 
    {
        this.file = new File(settingFileName);
        this.loadData();

        this.server = new ServerSocket(this.PORT);
        this.pool = Executors.newFixedThreadPool(this.THREAD_NUM);
        this.sender = new NotifySender(this.NOTIFY_PORT, InetAddress.getByName(this.NOTIFY_ADDRESS));
    }

    private void loadData() throws IOException {
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

            this.PORT = obj.get("server_port").getAsInt();
            this.THREAD_NUM = obj.get("thread_num").getAsInt();
            this.DATA_DIR = obj.get("data_dir").getAsString();
            this.NOTIFY_ADDRESS = obj.get("notify_address").getAsString();
            this.NOTIFY_PORT = obj.get("notify_port").getAsInt();
            this.DATA_SAVE_TIMEOUT = obj.get("data_save_timeout").getAsInt();
            this.NOTIFY_UPDATE_TIMEOUT = obj.get("notify_update_timeout").getAsInt();
        }
    }

    public void run()
            throws IOException, SecurityException, RejectedExecutionException, NullPointerException 
    {
        System.out.println("Running server in " + PORT);
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
