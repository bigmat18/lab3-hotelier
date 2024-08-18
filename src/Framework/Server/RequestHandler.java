package Framework.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements Runnable {
    private Socket connection;

    public RequestHandler(Socket connection) { 
        this.connection = connection; 
    }

    @Override
    public void run() {
        try (DataOutputStream output = new DataOutputStream(connection.getOutputStream());
             DataInputStream input = new DataInputStream(connection.getInputStream())) {

            while (!Thread.interrupted()) {
                try {
                    Request request = Message.read(Request.class, input);
                    Message.write(Response.class, output, Router.routing(request));
                } catch (Exception e) {
                    Message.write(Response.class, output, new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage()));
                }
            }
            this.connection.close();
        } catch (IOException e) {
            System.out.println("Connection closed with " + connection.getInetAddress());
        }
    }
}
