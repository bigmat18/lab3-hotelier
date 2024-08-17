package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Data.Hotel;
import Data.User;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

import java.net.Socket;

public class ClientMain {
    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8080;
    private static boolean running = true;
    private static boolean isLogged = false;
    private static String username = "";

    public static void main(String[] args) {

        try (Socket socket = new Socket(HOST, PORT);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to: " + HOST + ":" + PORT);

            System.out.print("\n" + //
                    "  _    _       _       _ _           \n" + //
                    " | |  | |     | |     | (_)          \n" + //
                    " | |__| | ___ | |_ ___| |_  ___ _ __ \n" + //
                    " |  __  |/ _ \\| __/ _ \\ | |/ _ \\ '__|\n" + //
                    " | |  | | (_) | ||  __/ | |  __/ |   \n" + //
                    " |_|  |_|\\___/ \\__\\___|_|_|\\___|_|   \n" + //
                    "                                     \n" + //
                    "                                     \n" + //
                    "");
            System.out.println("Digit one of following command or 'quit' to exit.\n\n" +
                               "   login\t\tLogin in the application\n" +
                               "   registraion\t\tRegistration with new account\n" +
                               "   logout\t\tLogout from application\n" +
                               "   searchHotel\t\tView hotel for name and city\n" +
                               "   searchAllHotel\tView all hotel in a city\n" +
                               "   insertReview\t\tAdd a review for a hotel\n" +
                               "   help\t\t\tView list of commands\n" + 
                               "   showBadge\t\tShow higher badge of user");

            while (running) {
                String choose = Keyboard.StringReader(">");
                switch (choose) {
                    case "quit": {
                        running = false;
                        System.out.println("Quitting...");
                        break;
                    }
                    case "registration": {
                        registration(output);

                        Response response = Message.read(Response.class, input);
                        if (response.statusCode.equals(Response.StatusCode.CREATED)) {
                            isLogged = true;
                            username = response.getBody().getAsJsonObject().get("message").getAsString();
                            System.out.println("Loegged successfull with "
                                    + response.getBody().getAsJsonObject().get("message").getAsString());
                        } else {
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        }
                        break;
                    }
                    case "login": {
                        login(output);

                        Response response = Message.read(Response.class, input);
                        if (response.statusCode.equals(Response.StatusCode.OK)) {
                            isLogged = true;
                            username = response.getBody().getAsJsonObject().get("message").getAsString();
                            System.out.println("Logged successfull with "
                                    + response.getBody().getAsJsonObject().get("message").getAsString());
                        } else {
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        }
                        break;
                    }
                    case "logout": {
                        logout(output);

                        Response response = Message.read(Response.class, input);
                        if (response.statusCode.equals(Response.StatusCode.OK))
                            isLogged = false;

                        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

                        break;
                    }
                    case "searchHotel": {
                        searchHotels(output);

                        Response response = Message.read(Response.class, input);
                        if (response.statusCode != Response.StatusCode.OK)
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

                        for (JsonElement element : response.getBody().getAsJsonArray()) {
                            Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
                            System.out.println("---------------------------------------------");
                            System.out.println(hotel.toString());
                        }

                        break;
                    }
                    case "searchAllHotel": {
                        searchAllHotels(output);

                        Response response = Message.read(Response.class, input);
                        if (response.statusCode != Response.StatusCode.OK)
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

                        for (JsonElement element : response.getBody().getAsJsonArray()) {
                            Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
                            System.out.println("---------------------------------------------");
                            System.out.println(hotel.toString());
                        }
                    }
                    case "insertReview": {
                        if (!isLogged)
                            System.out.println("You must be logged");
                        else {
                            insertReview(output);

                            Response response = Message.read(Response.class, input);
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        }

                        break;
                    }
                    case "showBadge": {
                        if (!isLogged)
                            System.out.println("You must be logged");
                        else {
                            showBadge(output);

                            Response response = Message.read(Response.class, input);
                            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
                        }
                        
                        break;
                    }
                    case "help": {
                        System.out.println("\nDigit one of following command or 'quit' to exit.\n\n" +
                                "   login\t\tLogin in the application\n" +
                                "   registraion\t\tRegistration with new account\n" +
                                "   logout\t\tLogout from application\n" +
                                "   searchHotel\t\tView hotel for name and city\n" +
                                "   searchAllHotel\tView all hotel in a city\n" +
                                "   insertReview\t\tAdd a review for a hotel\n" +
                                "   help\t\t\tView list of commands\n" +
                                "   showBadge\t\tShow higher badge of user");
                        break;
                    }
                    default: {
                        System.out.println("Invalid option");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login(DataOutputStream output) throws IOException {
        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Message.write(Request.class, output, new Request("/login", Request.Methods.POST, obj));
    }

    public static void registration(DataOutputStream output) throws IOException {
        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Message.write(Request.class, output, new Request("/registration", Request.Methods.POST, obj));
    }

    public static void logout(DataOutputStream output) throws IOException {
        Message.write(Request.class, output, new Request("/logout", Request.Methods.POST));
    }

    public static void searchHotels(DataOutputStream output) throws IOException {
        String name = Keyboard.StringReader("name: ");
        String city = Keyboard.StringReader("city: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("city", city);

        Message.write(Request.class, output, new Request("/hotels", Request.Methods.GET, obj));
    }

    public static void searchAllHotels(DataOutputStream output) throws IOException {
        String city = Keyboard.StringReader("city: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("city", city);
        obj.addProperty("ordering", true);

        Message.write(Request.class, output, new Request("/hotels", Request.Methods.GET, obj));
    }

    public static void insertReview(DataOutputStream output) throws IOException {
        String hotelCity = Keyboard.StringReader("city: ");
        String hotelName = Keyboard.StringReader("naame: ");

        int rate = Keyboard.IntReader("Rate: ");
        int positionRate = Keyboard.IntReader("position rate: ");
        int cleaningRate = Keyboard.IntReader("cleaning rate: ");
        int servicesRate = Keyboard.IntReader("service rate: ");
        int priceRate = Keyboard.IntReader("price rate: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("hotelCity", hotelCity);
        obj.addProperty("hotelName", hotelName);
        obj.addProperty("username", username);

        obj.addProperty("rate", rate);
        obj.addProperty("positionRate", positionRate);
        obj.addProperty("cleaningRate", cleaningRate);
        obj.addProperty("servicesRate", servicesRate);
        obj.addProperty("priceRate", priceRate);

        Message.write(Request.class, output, new Request("/reviews", Request.Methods.POST, obj));
    }

    public static void showBadge(DataOutputStream output) throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", username);
        Message.write(Request.class, output, new Request("/badge", Request.Methods.GET, obj));
    }
}
