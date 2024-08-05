package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class Router {
    private static HashMap<String, Endpoint> endpoints;
    
    public static void Inizialize() {
        endpoints = new HashMap<String, Endpoint>();
        endpoints.put("/test", new Endpoint());
    }

    public static void SendRequest(String url, String method, InputStream input, OutputStream output) {
        Endpoint ep;
        if((ep = endpoints.get(url)) != null) {
            switch (method) {
                case "GET": {
                    ep.GET(input, output); 
                    break;
                }
                case "POST": {
                    ep.POST(input, output); 
                    break;
                }
                case "DELETE": {
                    ep.DELETE(input, output); 
                    break;
                }
                case "PATCH": {
                    ep.PATCH(input, output); 
                    break;
                }
                default: {
                    NoMethodFound(output);
                    break;
                }
            }
        } else {
            NoPathFound(output);
        }
    }

    public static void NoMethodFound(OutputStream output) {

    }

    public static void NoPathFound(OutputStream output) {

    }

    public static void Shutdown() {
        endpoints.clear();
    }
}
