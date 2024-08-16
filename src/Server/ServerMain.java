package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Utils.Hotel;
import Utils.Request;
import Utils.Review;
import Utils.User;

public class ServerMain {
    private static boolean running = true;

    private static final int THREAD_POOL_NUM = 10;
    private static final int PORT = 8080;

    private static final int timeout = 5000;

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
            (new Rank()).start();
            
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
