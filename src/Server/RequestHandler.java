package Server;

import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket connection;
    
    RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        System.out.println("Handle request");
    }
}
