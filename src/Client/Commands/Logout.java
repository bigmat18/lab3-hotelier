package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import Client.AppStatus;
import Client.Command;
import Client.Keyboard;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class Logout extends Command {

    public Logout() {
        this.name = "logout";
        this.description = "\t\tLogout from application";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        Message.write(Request.class, output, new Request("/logout", Request.Methods.POST));

        Response response = Message.read(Response.class, input);
        if (response.statusCode.equals(Response.StatusCode.OK)) {
            status.setLogged(false);
            status.getNotifyThread().interrupt();
        }

        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
    }
}
