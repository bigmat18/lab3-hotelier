package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        try { 
            Server.Inizialize();
            Server.Run(); 
        } catch(IOException e) {
            Server.Shutdown();
        } finally {
            Server.Shutdown();
        }
    }
}
