package Server;

import java.util.HashMap;

public class Router {
    private static HashMap<String, Endpoint> endpoints;
    
    public static void Inizialize() {
        endpoints = new HashMap<>();

        endpoints.put("/test", new Endpoint());
    }

    public static void Shutdown() {
        endpoints.clear();
    }
}
