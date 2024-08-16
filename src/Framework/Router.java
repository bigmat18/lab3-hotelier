package Framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class Router {
    private static HashMap<String, Endpoint> endpoints;
    
    public static void inizialize() { endpoints = new HashMap<String, Endpoint>(); }

    public static void addEndpoint(String url, Endpoint ep) { endpoints.put(url, ep); }

    public static Response routing(Request request) throws IOException{
        Endpoint ep = endpoints.get(request.url);
        if(ep != null) {
            switch (request.method) {
                case GET:       return ep.GET(request);
                case POST:      return ep.POST(request); 
                case DELETE:    return ep.DELETE(request); 
                case PATCH:     return ep.PATCH(request); 
                default:        return new Response(Response.StatusCode.NOT_FOUND,
                                                    Response.StatusCode.NOT_FOUND.toString());
            }
        } else                  return new Response(Response.StatusCode.NOT_FOUND,
                                                    Response.StatusCode.NOT_FOUND.toString());
    }
}
