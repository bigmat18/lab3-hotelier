package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Utils.Request;
import Utils.Response;

import java.net.Socket;

public class ClientMain {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;
    private static boolean running = true;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            while (running) {
                Keyboard.StringReader();
                output.writeObject(new Request("/login", Request.Methods.GET));
                output.flush();

                Response response = (Response)input.readObject();
                System.out.println(response.statusCode.toString());
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
