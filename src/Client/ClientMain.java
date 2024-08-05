package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.net.Socket;


public class ClientMain {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            output.println("/test");

        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
