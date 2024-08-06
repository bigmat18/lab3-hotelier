package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static boolean running = true;

    private static final int THREAD_POOL_NUM = 10;
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        try(ServerSocket server = new ServerSocket(PORT)) {
            
            ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_NUM);

            Router.inizialize();
            Database.inizialize();

            System.out.println("Server running: " + server.getInetAddress().getHostName());
            
            while (running){
                Socket connection = server.accept();
                System.out.println("Connection from: " + connection.getInetAddress().getHostAddress());
                pool.execute(new RequestHandler(connection));
            }

        } finally {
            Database.shutdown();
        }
    }
}
