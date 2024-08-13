package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import Server.Endpoints.Hotels;
import Server.Endpoints.Login;
import Server.Endpoints.Logout;
import Server.Endpoints.Registration;
import Server.Endpoints.Reviews;
import Utils.Request;
import Utils.Response;

public class Router {
    private static HashMap<String, Endpoint> endpoints;
    
    public static void inizialize() {
        endpoints = new HashMap<String, Endpoint>();

        endpoints.put("/login", new Login());
        endpoints.put("/registration", new Registration());
        endpoints.put("/logout", new Logout());
        endpoints.put("/hotels", new Hotels());
        endpoints.put("/reviews", new Reviews());
    }

    public static Response routing(Request request) throws IOException{
        Endpoint ep = endpoints.get(request.url);
        if(ep != null) {
            switch (request.method) {
                case GET:       return ep.GET(request);
                case POST:      return ep.POST(request); 
                case DELETE:    return ep.DELETE(request); 
                case PATCH:     return ep.PATCH(request); 
                default:        return new Response(Response.StatusCode.NOT_FOUND,
                                                    Response.StatusCode.NOT_FOUND.name());
            }
        } else                  return new Response(Response.StatusCode.NOT_FOUND,
                                                    Response.StatusCode.NOT_FOUND.name());
    }
}
