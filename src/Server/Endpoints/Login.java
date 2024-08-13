package Server.Endpoints;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Server.Database;
import Server.Endpoint;
import Utils.Message;
import Utils.Request;
import Utils.Response;
import Utils.User;

public class Login extends Endpoint{

    @Override
    public Response POST(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                    .equals(data.get("username").getAsString()));

        if(users.isEmpty())
            return new Response(Response.StatusCode.BAD_REQUEST, "User not exists");
        
        if(!users.get(0).getPassword().equals(data.get("password").getAsString()))
            return new Response(Response.StatusCode.BAD_REQUEST, "Password incorrect");

        return new Response(Response.StatusCode.OK, "Login successfull");
    }
    
}
