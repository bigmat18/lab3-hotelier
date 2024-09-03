package ClientApp;

import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ClientApp.Commands.InsertReview;
import ClientApp.Commands.SearchAllHotels;
import ClientApp.Commands.SearchHotel;
import ClientApp.Commands.ShowBadge;
import ClientApp.Commands.Login;
import ClientApp.Commands.Registration;
import ClientApp.Commands.Logout;

import Framework.Notify.NotifyReciever;



public class ClientMain {
    private static String TCP_ADDRESS;
    private static int TCP_PORT;

    private static String NOTIFY_HOST;
    private static int NOTIFY_PORT;
    private static int NOTIFY_TIMEOUT;

    private static final String CONFIG_FILE_NAME = "config_client.json";

    public static void main(String[] args) {
        try {
            Inizialize();
        } catch (Exception e) {
            System.out.println("[ERROR] Error to load " + CONFIG_FILE_NAME + " (" + e.getLocalizedMessage() + ")");
            return;
        }

        try (Socket socket = new Socket(TCP_ADDRESS, TCP_PORT);
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

            RankingNotify notify = new RankingNotify(NOTIFY_HOST, NOTIFY_PORT, NOTIFY_TIMEOUT);
            Thread thread = new Thread(notify);
            thread.start();

            AppStatus status = new AppStatus();
            status.setNotify(notify);
            
            CommandHandler cmdHandler = new CommandHandler(status);
            cmdHandler.addCommand(new Login());
            cmdHandler.addCommand(new Registration());
            cmdHandler.addCommand(new Logout());
            cmdHandler.addCommand(new SearchHotel());
            cmdHandler.addCommand(new SearchAllHotels());
            cmdHandler.addCommand(new InsertReview());
            cmdHandler.addCommand(new ShowBadge());

            System.out.println(cmdHandler.getCommandsList());

            while (status.isRunning()) {
                cmdHandler.execute(input, output);
            }
            thread.interrupt();

        } catch (Exception e) {
            System.out.println("[ERROR] Error in app (" + e.getLocalizedMessage() + ")");
        }
    }

    public static void Inizialize() throws IOException {
        File file = new File("config_client.json");
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

            TCP_ADDRESS = obj.get("tcp_address").getAsString();
            TCP_PORT = obj.get("tcp_port").getAsInt();
            NOTIFY_HOST = obj.get("notify_address").getAsString();
            NOTIFY_PORT = obj.get("notify_port").getAsInt();
            NOTIFY_TIMEOUT = obj.get("notify_timeout").getAsInt();
        }
    }
}
