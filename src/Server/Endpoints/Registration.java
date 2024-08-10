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
        JsonObject response = new JsonObject();

        ArrayList<User> users = Database.select(User.class, entry -> entry.getValue().getUsername().equals(user.getUsername()));
        if(!users.isEmpty()) {
            response.addProperty("message", "Username altredy exists");
            return new Response(Response.StatusCode.BAD_REQUEST, response);
        }


        if(!user.isPasswordValid()) {
            response.addProperty("message", "Password not valid");
            return new Response(Response.StatusCode.BAD_REQUEST, response);
        }

        try {
            Database.insert(User.class, user.getPassword(), user.getUsername());

            response.addProperty("message", "User created");
            return new Response(Response.StatusCode.CREATED, response);
        } catch(Exception e) {
            response.addProperty("message", e.getMessage());
            return new Response(Response.StatusCode.CREATED, response);
        }
    }
}
