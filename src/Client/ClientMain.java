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

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            output.writeObject(new Request("/login", Request.Methods.GET));
            Response response = (Response)input.readObject();
            System.out.println(response.statusCode.toString());

            output.flush();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
