package ServerApp;

import com.google.gson.JsonObject;

import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Logout extends Endpoint {

    @Override
    public Response POST(Request request) {
        JsonObject obj = request.getBody().getAsJsonObject();
        try {
            removeSession(obj.get("token").getAsString());
            return new Response(Response.StatusCode.OK, "Logout successfull");
        } catch (Exception e) {
            return new Response(Response.StatusCode.BAD_REQUEST, e.getLocalizedMessage());
        }
    }
}
