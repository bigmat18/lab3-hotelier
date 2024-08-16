package Framework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements Runnable {
    private Socket connection;
    private boolean running = true;

    public RequestHandler(Socket connection) { this.connection = connection; }

    @Override
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
