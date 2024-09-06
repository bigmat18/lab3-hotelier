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
            if(!removeSession(obj.get("token").getAsString()))
                return new Response(Response.StatusCode.OK, "[Error] Logout failed");

            return new Response(Response.StatusCode.OK, "[Ok] Logout successful");
        } catch (Exception e) {
            return new Response(Response.StatusCode.BAD_REQUEST, e.getLocalizedMessage());
        }
    }
}
