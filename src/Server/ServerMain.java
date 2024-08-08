package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.Tables.User;

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

            int id = Database.getLastTableId(User.class);
            Database.insert(User.class, new User(id + 1, "123", "mat.giu2002@gmail.com"));
            Database.delete(User.class, id);

            System.out.println("Server running: " + server.getInetAddress().getHostName());
            
            while (running){
                Socket connection = server.accept();
                System.out.println("Connection from: " + connection.getInetAddress().getHostAddress());
                pool.execute(new RequestHandler(connection));
            }
        } catch(Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
