package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Client.AppStatus;
import Client.Command;
import Client.Keyboard;
import Data.Hotel;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class ShowBadge extends Command {

    public ShowBadge() {
        this.name = "showBadge";
        this.description = "\t\tShow higher badge of user";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        if (!status.isLogged()) {
            System.out.println("You must be logged");
            return;
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("username", status.getUsername());
        Message.write(Request.class, output, new Request("/badge", Request.Methods.GET, obj));

        Response response = Message.read(Response.class, input);
        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
    }
}
