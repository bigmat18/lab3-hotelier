package Server;

import Utils.Request;
import Utils.Response;
import Utils.User;

public class Endpoint {

    public Response GET(Request request) {
        User user = request.getBody(User.class);
        System.out.println(user.getId());
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }
    
    public Response POST(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }

    public Response DELETE(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }

    public Response PATCH(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }
}
