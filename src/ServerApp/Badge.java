package ServerApp;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import Data.User;
import Framework.Database.Database;
import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Badge extends Endpoint {
    public Response GET(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();

        try {
            ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                        .equals(data.get("username")
                                                                                                    .getAsString()));

            if (users.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "User doen't exist");

            return new Response(Response.StatusCode.OK, users.get(0).getBandge());
        } catch (Exception e) {
            return new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
