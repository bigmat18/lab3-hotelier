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

public class Login extends Command {
    
    public Login() {
        this.name = "login";
        this.description = "\t\tLogin in the application";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        if(status.isLogged()) {
            System.out.println("User alredy logged. Execute 'logout' firt to login with new user");
            return;
        }

        JsonObject obj = new JsonObject();
        String username = Keyboard.StringReader("username: ");
        String password = Keyboard.StringReader("password: ");

        obj.addProperty("password", password);
        obj.addProperty("username", username);

        Message.write(Request.class, output, new Request("/login", Request.Methods.POST, obj));

        Response response = Message.read(Response.class, input);
        if (response.statusCode.equals(Response.StatusCode.OK)) {
            status.setLogged(true);
            status.setUsername(username);
            status.getNotifyThread().start();
        } 

        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
    }
}
