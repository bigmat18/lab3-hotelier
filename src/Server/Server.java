package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static Router router;
    private static ServerSocket listener;
    private static ExecutorService pool;
    private static boolean running = true;

    private static final int THREAD_POOL_NUM = 10;
    private static final int PORT = 10;

    public static void Inizialize() throws IOException {
        listener = new ServerSocket(PORT);
        pool = Executors.newFixedThreadPool(THREAD_POOL_NUM);

        Router.Inizialize();
        Database.Inizialize();

        System.out.println("Server inizialize: " + listener.getInetAddress().getHostName());
    }

    public static void Shutdown() {
        System.out.println("Server shutdown: " + listener.getInetAddress().getHostName());

        listener.close();
        pool.shutdown();
        
        Router.Shutdown();
        Database.Shutdown();
    }

    public static void Run() throws IOException{
        System.out.println("Server running: " + listener.getInetAddress().getHostName());
        while (running){
            Socket connection = listener.accept();
            System.out.println("Connection from: " + connection.getInetAddress().getHostAddress());
            pool.execute(new RequestHandler(connection));
        }
    }
}
