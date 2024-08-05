package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Utils.Request;

public class RequestHandler implements Runnable {
    private Socket connection;

    RequestHandler(Socket connection) { this.connection = connection; }

    public void run() {
        try (Scanner input = new Scanner(this.connection.getInputStream());
             ObjectInputStream input2 = new ObjectInputStream(this.connection.getInputStream())) {

            String url = input.nextLine();
            String method = input.nextLine();
            Request request = (Request)input2.readObject();

            System.out.println(request.url + " " + request.method);
            this.connection.close();

        } catch (Exception e) {}
    }
}
