package Framework.Server;

import com.google.gson.JsonObject;

public class Response extends Message {
    // Enum che definisce i possibili codici di stato HTTP con codice numerico e
    // descrizione
    public enum StatusCode {
        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
        NO_CONTENT(204, "No Content"),

        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        PAYMENT_REQUIRED(402, "Payment Required"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented");

        // Codice di stato numerico
        public final int code;
        // Descrizione del codice di stato
        public final String reasonPhrase;

        // Costruttore dell'enum StatusCode
        StatusCode(int code, String reasonPhrase) {
            this.code = code;
            this.reasonPhrase = reasonPhrase;
        }

        // Override del metodo toString per restituire una rappresentazione leggibile
        // del codice di stato
        @Override
        public String toString() {
            return code + " - " + reasonPhrase;
        }
    }

    // Codice di stato della risposta
    public StatusCode statusCode;

    // Costruttore che accetta solo il codice di stato
    public Response(StatusCode code) {
        this.statusCode = code;
    }

    // Costruttore che accetta il codice di stato e un messaggio
    public Response(StatusCode code, String message) {
        super(message); // Passa il messaggio al costruttore della superclasse Message
        this.statusCode = code;
    }

    // Costruttore che accetta il codice di stato e un corpo di tipo Object
    public Response(StatusCode code, Object body) {
        super(body); // Passa il corpo al costruttore della superclasse Message
        this.statusCode = code;
    }

    // Costruttore che accetta il codice di stato e un corpo in formato JsonObject
    public Response(StatusCode code, JsonObject body) {
        super(body); // Passa il corpo al costruttore della superclasse Message
        this.statusCode = code;
    }
}
