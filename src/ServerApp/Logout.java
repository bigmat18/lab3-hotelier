package ServerApp;

import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Logout extends Endpoint {

    @Override
    public Response POST(Request request) {
        return new Response(Response.StatusCode.OK, "Logout successfull");
    }
}
