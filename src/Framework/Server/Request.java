package Framework.Server;

import com.google.gson.JsonObject;

public class Request extends Message {
    // Enum che definisce i metodi HTTP supportati (GET, POST, DELETE, PATCH)
    public enum Methods {
        GET, POST, DELETE, PATCH
    };

    // URL della richiesta
    public String url;
    // Metodo HTTP della richiesta (uno dei metodi definiti nell'enum)
    public Methods method;

    // Costruttore che accetta solo URL e metodo
    public Request(String url, Methods method) {
        super(); // Chiama il costruttore della superclasse (Message)
        this.url = url;
        this.method = method;
    }

    // Costruttore che accetta URL, metodo e un corpo (di tipo Object)
    public Request(String url, Methods method, Object body) {
        super(body); // Passa il corpo alla superclasse (Message)
        this.url = url;
        this.method = method;
    }

    // Costruttore che accetta URL, metodo e un corpo in formato JsonObject
    public Request(String url, Methods method, JsonObject body) {
        super(body); // Passa il corpo alla superclasse (Message)
        this.url = url;
        this.method = method;
    }
}
