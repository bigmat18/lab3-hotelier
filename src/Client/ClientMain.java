package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Data.Hotel;
import Data.User;
import Framework.Database.Database;
import Framework.Notify.NotifyReciever;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

import java.net.Socket;

public class ClientMain {
    private static final String TCP_HOST = "0.0.0.0";
    private static final int TCP_PORT = 8080;

    private static final String UDP_HOST = "239.0.0.1";
    private static final int UDP_PORT = 8888;
    private static final int UDP_TIMEOUT = 4000;

    public static void main(String[] args) {

        try (Socket socket = new Socket(TCP_HOST, TCP_PORT);
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

            Thread notify = new Thread() {
                @Override
                public void run() {
                    try (NotifyReciever socket = new NotifyReciever(UDP_PORT, InetAddress.getByName(UDP_HOST),
                            UDP_TIMEOUT)) {
                        while (!Thread.interrupted()) {
                            try {
                                byte[] received = socket.receiveNotify();
                                if (received != null)
                                    System.out.print("\033[0K\r[NEWS] - " + (new String(received)) + "\n> ");

                            } catch (SocketTimeoutException e) {
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            AppStatus status = new AppStatus();
            status.setNotifyThread(notify);
            
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
            notify.interrupt();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
