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

import Utils.Message;
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
                System.out.print("");
                String keyInput = Keyboard.StringReader("To stop digit 'q': ");
                running = !keyInput.equals("q");

                JsonObject obj = new JsonObject();
                obj.addProperty("password", "Admin123456@");
                obj.addProperty("username", "Admin");

                Request request = new Request("/login", Request.Methods.POST, obj);
                output.writeUTF(request.getString());
                output.flush();

                Response response = Message.getMessage(Response.class, input.readUTF());
                System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
