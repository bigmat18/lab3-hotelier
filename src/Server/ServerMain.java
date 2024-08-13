package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Utils.Request;
import Utils.User;

public class ServerMain {
    private static boolean running = true;

    private static final int THREAD_POOL_NUM = 10;
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown...");
                try { Database.shutdown(); }
                catch(Exception e) { System.out.println(e.getLocalizedMessage()); }
            }
        });

        try(ServerSocket server = new ServerSocket(PORT)) {
            
            ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_NUM);

            Router.inizialize();
            Database.inizialize();

            System.out.println("Server running: " + server.getInetAddress().getHostName());
            
            while (running){
                Socket connection = server.accept();
                System.out.println("Connection opened with: " + connection.getInetAddress());
                pool.execute(new RequestHandler(connection));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
