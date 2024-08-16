package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Framework.Database;
import Framework.Rank;
import Framework.RequestHandler;
import Framework.Router;

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
                try { 
                    Database.shutdown(); 
                } catch(Exception e) { 
                    e.printStackTrace(); 
                }
            }
        });

        try(ServerSocket server = new ServerSocket(PORT)) {
            
            System.out.println("Starting...");
            ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_NUM);

            Router.inizialize();
            Database.inizialize();
            
            Router.addEndpoint("/login", new Login());
            Router.addEndpoint("/registration", new Registration());
            Router.addEndpoint("/logout", new Logout());
            Router.addEndpoint("/hotels", new Hotels());
            Router.addEndpoint("/reviews", new Reviews());

            Thread thread = new Thread(new Rank());
            thread.start();
            
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
