package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Utils.Request;

import java.net.Socket;


public class ClientMain {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             ObjectOutputStream output2 = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            Request request = new Request("ciao", Request.Methods.DELETE);
            output2.writeObject(request);

        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
