package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import Utils.Message;
import Utils.Request;
import Utils.Response;
import Utils.User;

public class RequestHandler implements Runnable {
    private Socket connection;
    private boolean running = true;

    RequestHandler(Socket connection) { this.connection = connection; }

    public void run() {
        try (DataOutputStream output = new DataOutputStream(connection.getOutputStream());
             DataInputStream input = new DataInputStream(connection.getInputStream())) {

            while (this.running) {
                Request request = Message.getMessage(Request.class, input.readUTF());
                Response response = Router.routing(request);
                System.out.println(response.statusCode.toString());
            }
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
