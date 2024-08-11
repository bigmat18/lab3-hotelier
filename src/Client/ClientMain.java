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
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import Utils.Hotel;
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
                int choose = Keyboard.IntReader("1) Registration\n" +
                                                "2) Login\n" + 
                                                "3) Logut\n" +
                                                "4) Search hotels\n" + 
                                                "5) Search all hotels\n" +
                                                "Choose option (0 to quit):");
                switch(choose) {
                    case 0: {
                        running = false;
                        System.out.println("Quitting...");
                        break;
                    }
                    case 1: {
                        registration(output);

                        Response response = Message.getMessage(Response.class, read(input));
                        if(response.statusCode.equals(Response.StatusCode.CREATED))
                            isLogged = true;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        break;
                    }
                    case 2: {
                        login(output);

                        Response response = Message.getMessage(Response.class, read(input));
                        if (response.statusCode.equals(Response.StatusCode.OK))
                            isLogged = true;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        break;
                    }
                    case 3: {
                        logout(output);

                        Response response = Message.getMessage(Response.class, read(input));
                        if (response.statusCode.equals(Response.StatusCode.OK))
                            isLogged = false;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                    }
                    case 4: {
                        searchHotels(output);

                        Response response = Message.getMessage(Response.class, read(input));
                        if(response.statusCode != Response.StatusCode.OK)
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

                        for(JsonElement element : response.getBody().getAsJsonArray()) {
                            Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
                            System.out.println("---------------------------------------------");
                            System.out.println(hotel.toString());
                        }
                    }
                    case 5: {
                        searchAllHotels(output);

                        Response response = Message.getMessage(Response.class, read(input));
                        if (response.statusCode != Response.StatusCode.OK)
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

                        for (JsonElement element : response.getBody().getAsJsonArray()) {
                            Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
                            System.out.println("---------------------------------------------");
                            System.out.println(hotel.toString());
                        }
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
        write(output, request.getString());
    }

    public static void registration(DataOutputStream output) throws IOException{
        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Request request = new Request("/registration", Request.Methods.POST, obj);
        write(output, request.getString());
    }

    public static void logout(DataOutputStream output) throws IOException {
        Request request = new Request("/logout", Request.Methods.POST);
        write(output, request.getString());
    }

    public static void searchHotels(DataOutputStream output) throws IOException {
        String name = Keyboard.StringReader("Filter by name (empty if dont't want): ");
        String city = Keyboard.StringReader("Filter by city (empty if dont't want): ");

        JsonObject obj = new JsonObject();
        if(!name.equals("")) obj.addProperty("name", name);
        if(!city.equals("")) obj.addProperty("city", city);

        Request request = new Request("/hotels", Request.Methods.GET, obj);
        write(output, request.getString());
    }

    public static void searchAllHotels(DataOutputStream output) throws IOException {
        String city = Keyboard.StringReader("Insert city: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("city", city);
        obj.addProperty("ordering", true);

        Request request = new Request("/hotels", Request.Methods.GET, obj);
        write(output, request.getString());
    }

    private static String read(DataInputStream input) throws IOException {
        int length = input.readInt();
        byte[] stringBytes = new byte[length];
        input.readFully(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    private static void write(DataOutputStream output, String data) throws IOException {
        byte[] bytesResponse = data.getBytes();
        output.writeInt(bytesResponse.length);
        output.write(bytesResponse);
        output.flush();
    }
}
