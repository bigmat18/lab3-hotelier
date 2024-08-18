package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Client.Commands.ShowBadge;
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

            AppStatus status = new AppStatus();
            CommandHandler cmdHandler = new CommandHandler(status);
            cmdHandler.addCommand(new Login());
            cmdHandler.addCommand(new Registration());
            cmdHandler.addCommand(new Logout());
            cmdHandler.addCommand(new SearchHotel());
            cmdHandler.addCommand(new SearchAllHotels());
            cmdHandler.addCommand(new ShowBadge());

            System.out.println(cmdHandler.getCommandsList());

            while(status.isRunning()) {
                cmdHandler.execute(input, output);
            }
  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
