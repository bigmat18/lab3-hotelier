package Framework.Server;

import java.io.IOException;
import java.util.HashMap;

public class Router {
    private static HashMap<String, Endpoint> endpoints = new HashMap<String, Endpoint>();;
    private static boolean isInit = false;

    public static void inizialize() { 
        isInit = true;
        System.out.println("[ROUTER] Inizialization completed.");
    }

    public static void addEndpoint(String url, Endpoint ep) throws RouterInizializeException{ 
        if(isInit)
            throw new RouterInizializeException("After endpoint inizialization you shoudn't add endpoint");
            
        endpoints.put(url, ep); 
    }

    public static Response routing(Request request) 
        throws IOException, RouterInizializeException 
    {
        if(!isInit)
            throw new RouterInizializeException("Router must be inizialized");

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
