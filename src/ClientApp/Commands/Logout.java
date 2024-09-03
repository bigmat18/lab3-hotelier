package ClientApp.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ClientApp.AppStatus;
import ClientApp.Command;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class Logout extends Command {

    public Logout() {
        this.name = "logout";
        this.description = "\t\tLogout from application";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        JsonObject obj = new JsonObject();
        obj.addProperty("token", status.getToken());

        Message.write(Request.class, output, new Request("/logout", Request.Methods.POST, obj));

        Response response = Message.read(Response.class, input);
        if (response.statusCode.equals(Response.StatusCode.OK)) {
            status.setToken(null);
            status.getNotify().leaveGroup();
        }

        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
    }
}
