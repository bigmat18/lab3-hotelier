package ClientApp.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ClientApp.AppStatus;
import ClientApp.Command;
import ClientApp.Keyboard;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class Registration extends Command {

    public Registration() {
        this.name = "registration";
        this.description = "\t\tRegistration with new account";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        if (status.isLogged()) {
            System.out.println("User alredy logged. Execute 'logout' firt to register with new user");
            return;
        }

        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Message.write(Request.class, output, new Request("/registration", Request.Methods.POST, obj));

        Response response = Message.read(Response.class, input);
        if (response.statusCode.equals(Response.StatusCode.CREATED)) {
            status.setLogged(true);
            status.setUsername(username);
            status.getNotifyThread().start();
        } 
        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
    }
}
