package Server;

import Framework.Endpoint;
import Framework.Request;
import Framework.Response;

public class Logout extends Endpoint {

    @Override
    public Response POST(Request request) {
        return new Response(Response.StatusCode.OK, "Logout successfull");
    }
}
