package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import Utils.Request;
import Utils.Response;

public class Router {
    private static HashMap<String, Endpoint> endpoints;
    
    public static void inizialize() {
        endpoints = new HashMap<String, Endpoint>();

        endpoints.put("/login", new Endpoint());
        endpoints.put("/registration", new Endpoint());
        endpoints.put("/logout", new Endpoint());
    }

    public static void routing(Request request, ObjectInputStream input, ObjectOutputStream output) throws IOException{
        Endpoint ep = endpoints.get(request.url);
        if(ep != null) {
            switch (request.method) {
                case GET: {
                    Response response = ep.GET(input); 
                    output.writeObject(response);
                    break;
                }
                case POST: {
                    Response response = ep.POST(input); 
                    output.writeObject(response);
                    break;
                }
                case DELETE: {
                    Response response = ep.DELETE(input); 
                    output.writeObject(response);
                    break;
                }
                case PATCH: {
                    Response response = ep.PATCH(input); 
                    output.writeObject(response);
                    break;
                }
                default: {
                    output.writeObject(new Response(Response.StatusCode.METHOD_NOT_ALLOWED));
                    break;
                }
            }
        } else {
            output.writeObject(new Response(Response.StatusCode.NOT_FOUND));
        }
    }
}
