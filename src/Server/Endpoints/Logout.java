package Server.Endpoints;

import Server.Endpoint;
import Utils.Request;
import Utils.Response;

public class Logout extends Endpoint {

    @Override
    public Response POST(Request request) {
        return new Response(Response.StatusCode.OK, "Logout successfull");
    }
}
