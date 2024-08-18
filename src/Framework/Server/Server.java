package Framework.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import Data.Hotel;
import Data.Review;
import Data.User;
import Framework.Database.Database;

public class Server implements AutoCloseable {
    private final int PORT;
    private final int THREAD_NUM;

    private ServerSocket server;
    private ExecutorService pool;

    public Server(int port, int threadNum)
            throws IOException, SecurityException, IllegalArgumentException {
        this.PORT = port;
        this.THREAD_NUM = threadNum;
        this.server = new ServerSocket(this.PORT);
        this.pool = Executors.newFixedThreadPool(threadNum);
    }

    public void run()
            throws IOException, SecurityException, RejectedExecutionException, NullPointerException {

        System.out.println("Running server in " + PORT);
        while (!Thread.interrupted()) {
            Socket connection = this.server.accept();
            System.out.println("Connection opened with: " + connection.getInetAddress());
            this.pool.execute(new RequestHandler(connection));
        }
    }

    public void close() throws SecurityException, IOException {
        System.out.println("ciao");
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
