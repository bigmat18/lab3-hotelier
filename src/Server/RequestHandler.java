package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RequestHandler implements Runnable {
    private Socket connection;

    RequestHandler(Socket connection) { this.connection = connection; }

    public void run() {
        try (Scanner input = new Scanner(this.connection.getInputStream())) {
            
            this.connection.close();

        } catch (IOException e) {}
    }
}
