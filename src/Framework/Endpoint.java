package Framework;

import Data.User;

public class Endpoint {

    public Response GET(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.reasonPhrase);
    }
    
    public Response POST(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.reasonPhrase);
    }

    public Response DELETE(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.reasonPhrase);
    }

    public Response PATCH(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.reasonPhrase);
    }
}
