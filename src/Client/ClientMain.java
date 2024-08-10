package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    private static boolean isLogged = false;

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            while (running) {
                System.out.println("\n================================");
                int choose = Keyboard.IntReader("1) Registration\n2) Login\n3) Logut\nChoose option (0 to quit):");
                switch(choose) {
                    case 0: {
                        running = false;
                        System.out.println("Quitting...");
                        break;
                    }
                    case 1: {
                        registration(output);

                        Response response = Message.getMessage(Response.class, input.readUTF());
                        if(response.statusCode.equals(Response.StatusCode.CREATED))
                            isLogged = true;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        break;
                    }
                    case 2: {
                        login(output);

                        Response response = Message.getMessage(Response.class, input.readUTF());
                        if (response.statusCode.equals(Response.StatusCode.OK))
                            isLogged = true;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        break;
                    }
                    case 3: {
                        logout(output);

                        Response response = Message.getMessage(Response.class, input.readUTF());
                        if (response.statusCode.equals(Response.StatusCode.OK))
                            isLogged = false;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                    }
                }

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void login(DataOutputStream output) throws IOException{
        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Request request = new Request("/login", Request.Methods.POST, obj);
        output.writeUTF(request.getString());
        output.flush();
    }

    public static void registration(DataOutputStream output) throws IOException{
        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Request request = new Request("/registration", Request.Methods.POST, obj);
        output.writeUTF(request.getString());
        output.flush();
    }

    public static void logout(DataOutputStream output) throws IOException {
        Request request = new Request("/logout", Request.Methods.POST);
        output.writeUTF(request.getString());
        output.flush();
    }
}
