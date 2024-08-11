package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
                try {
                    Request request = Message.getMessage(Request.class, this.read(input));
                    Response response = Router.routing(request);

                    this.write(output, response.getString());
                } catch (Exception e) {
                    Response response = new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());

                    this.write(output, response.getString());
                    e.printStackTrace();
                }
            }
            this.connection.close();
        } catch (IOException e) {
            System.out.println("Connection closed with " + connection.getInetAddress());
        }
    }

    private String read(DataInputStream input) throws IOException {
        int length = input.readInt();
        byte[] stringBytes = new byte[length];
        input.readFully(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    private void write(DataOutputStream output, String data) throws IOException {
        byte[] bytesResponse = data.getBytes();
        output.writeInt(bytesResponse.length);
        output.write(bytesResponse);
        output.flush();
    }
}
