package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Utils.Response;

public class Endpoint {

    public Response GET(ObjectInputStream request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }
    
    public Response POST(ObjectInputStream request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }

    public Response DELETE(ObjectInputStream request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }

    public Response PATCH(ObjectInputStream request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED);
    }
}
