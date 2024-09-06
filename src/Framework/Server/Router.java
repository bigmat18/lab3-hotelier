package Framework.Server;

import java.io.IOException;
import java.util.HashMap;

public class Router {
    // Mappa che associa URL a oggetti Endpoint
    private static HashMap<String, Endpoint> endpoints = new HashMap<String, Endpoint>();
    // Flag che indica se il router è stato inizializzato
    private static boolean isInit = false;

    // Metodo per inizializzare il router
    public static void inizialize() {
        isInit = true;
        System.out.println("[ROUTER] Inizialization completed.");
    }

    // Aggiunge un endpoint alla mappa degli endpoint
    public static void addEndpoint(String url, Endpoint ep) throws RouterInizializeException {
        // Non è possibile aggiungere endpoint dopo l'inizializzazione del router
        if (isInit)
            throw new RouterInizializeException("After endpoint inizialization you shoudn't add endpoint");

        // Aggiunge l'endpoint alla mappa degli endpoint con la chiave dell'URL
        endpoints.put(url, ep);
    }

    // Metodo per effettuare il routing delle richieste
    public static Response routing(Request request)
            throws IOException, RouterInizializeException {
        // Controlla se il router è stato inizializzato
        if (!isInit)
            throw new RouterInizializeException("Router must be inizialized");

        // Cerca l'endpoint corrispondente all'URL della richiesta
        Endpoint ep = endpoints.get(request.url);

        // Se l'endpoint è trovato, invoca il metodo corretto in base al metodo HTTP
        // della richiesta
        if (ep != null) {
            switch (request.method) {
                case GET:
                    return ep.GET(request);
                case POST:
                    return ep.POST(request);
                case DELETE:
                    return ep.DELETE(request);
                case PATCH:
                    return ep.PATCH(request);
                default:
                    return new Response(Response.StatusCode.NOT_FOUND,
                            Response.StatusCode.NOT_FOUND.toString());
            }
        } else {
            // Se l'endpoint non esiste, restituisce un errore 404
            return new Response(Response.StatusCode.NOT_FOUND,
                    Response.StatusCode.NOT_FOUND.toString());
        }
    }
}
