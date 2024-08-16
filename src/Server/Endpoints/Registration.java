package Server.Endpoints;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Server.Database;
import Server.Endpoint;
import Utils.Request;
import Utils.Response;
import Utils.User;

public class Registration extends Endpoint {

    @Override
    public Response POST(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                    .equals(data.get("username")
                                                                                                .getAsString()));

        if(!users.isEmpty())
            return new Response(Response.StatusCode.BAD_REQUEST, "Username altredy exists");

        if(!User.isPasswordValid(data.get("password").getAsString()))
            return new Response(Response.StatusCode.BAD_REQUEST, "Password not valid (it must contains a capital letter, a number, "+
                                                                  "a special character and it must be at least 8 characters long)");

        try {
            Database.insert(User.class,
                            data.get("password").getAsString(), 
                            data.get("username").getAsString());
                            
            return new Response(Response.StatusCode.CREATED, data.get("username").getAsString());
        } catch(Exception e) {
            return new Response(Response.StatusCode.BAD_REQUEST,  e.getMessage());
        }
    }
}
