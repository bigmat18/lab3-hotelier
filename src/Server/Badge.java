package Server;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import Data.User;
import Framework.Database;
import Framework.Endpoint;
import Framework.Message;
import Framework.Request;
import Framework.Response;

public class Badge extends Endpoint{
    public Response GET(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                    .equals(data.get("username")
                                                                                                .getAsString()));
        if(users.isEmpty())
            return new Response(Response.StatusCode.BAD_REQUEST, "User doen't exist");

        return new Response(Response.StatusCode.OK, users.get(0).getBandge());
    }
}
