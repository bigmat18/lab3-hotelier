package ServerApp;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import Data.User;
import Framework.Database.Database;
import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Registration extends Endpoint {

    @Override
    public Response POST(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        try {
            ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                        .equals(data.get("username")
                                                                                                    .getAsString()));

            if(!users.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "Username altredy exists");

            if(!User.isPasswordValid(data.get("password").getAsString()))
                return new Response(Response.StatusCode.BAD_REQUEST, "Password not valid (it must contains a capital letter, a number, "+
                                                                    "a special character and it must be at least 8 characters long)");

            Database.insert(User.class,
                            data.get("password").getAsString(), 
                            data.get("username").getAsString());

            String token = createSession(data.get("username").getAsString());
            if (token == null)
                return new Response(Response.StatusCode.BAD_REQUEST, "Error in session creation");

            JsonObject response = new JsonObject();
            response.addProperty("message", "Registration successfull with " + data.get("username").getAsString());
            response.addProperty("token", token);
                            
            return new Response(Response.StatusCode.CREATED, response);
        } catch(Exception e) {
            return new Response(Response.StatusCode.INTERNAL_SERVER_ERROR,  e.getMessage());
        }
    }
}
