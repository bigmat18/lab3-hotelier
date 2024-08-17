package Framework.Server;

import Data.User;

public class Endpoint {

    public Response GET(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }
    
    public Response POST(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    public Response DELETE(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    public Response PATCH(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }
}
