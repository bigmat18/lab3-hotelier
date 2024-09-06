package ServerApp;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import Data.User;
import Framework.Database.Database;
import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Login extends Endpoint {

    @Override
    public Response POST(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();

        try {
            ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername()
                                                                                        .equals(data.get("username")
                                                                                        .getAsString()));

            if (users.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "[Error] User doesn't exists");

            if (!users.get(0).getPassword().equals(data.get("password").getAsString()))
                return new Response(Response.StatusCode.BAD_REQUEST, "[Error] Password isn't correct");

            String token = createSession(data.get("username").getAsString());
            if(token == null)
                return new Response(Response.StatusCode.BAD_REQUEST, "[Error] Problem session creation, probably someone is just logged with this account");

            JsonObject response = new JsonObject();
            response.addProperty("message", "[Ok] Login sucessfull with " + data.get("username").getAsString());
            response.addProperty("token", token);
            return new Response(Response.StatusCode.OK, response);
            
        } catch (Exception e) {
            return new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

}
