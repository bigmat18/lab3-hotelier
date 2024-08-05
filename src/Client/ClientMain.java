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
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);
            while(true) {
                int result = Keyboard.IntReader("Inserisci intero: ");
                output.writeInt(result);
                output.flush();
            }

        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
