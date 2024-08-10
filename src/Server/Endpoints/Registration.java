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
        User user = request.getBody(User.class);

        ArrayList<User> users = Database.select(User.class, entry -> entry.getValue().getUsername().equals(user.getUsername()));

        if(!users.isEmpty())
            return new Response(Response.StatusCode.BAD_REQUEST, "Username altredy exists");

        if(!user.isPasswordValid())
            return new Response(Response.StatusCode.BAD_REQUEST, "Password not valid");

        try {
            Database.insert(User.class, user.getPassword(), user.getUsername());
            return new Response(Response.StatusCode.CREATED, "User created");
        } catch(Exception e) {
            return new Response(Response.StatusCode.CREATED,  e.getMessage());
        }
    }
}
