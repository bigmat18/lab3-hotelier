package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Utils.Request;

public class RequestHandler implements Runnable {
    private Socket connection;
    private boolean running = true;

    RequestHandler(Socket connection) { this.connection = connection; }

    public void run() {
        try (ObjectOutputStream output = new ObjectOutputStream(this.connection.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(this.connection.getInputStream())) {

            while (this.running) {
                Router.routing((Request) input.readObject(), input, output);
                System.out.println("Roting from " + this.connection.getInetAddress());
            }
            this.connection.close();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
