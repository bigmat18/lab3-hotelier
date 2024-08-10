package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import Utils.Request;
import Utils.Response;
import Utils.User;

import java.net.Socket;

public class ClientMain {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;
    private static boolean running = true;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            while (running) {
                Keyboard.StringReader();

                User user = new User(1, "ciao", "ciao");
                Request request = new Request("/login", Request.Methods.GET, user);
                output.writeUTF(request.getString());
                output.flush();

                // Response response = (Response)input.readObject();
                // System.out.println(response.statusCode.toString());
            }

        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }
    }
}
