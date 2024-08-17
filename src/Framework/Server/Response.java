package Framework.Server;

import com.google.gson.JsonObject;

public class Response extends Message {
    public enum StatusCode {
        OK                              (200, "OK"),
        CREATED                         (201, "Created"),
        ACCEPTED                        (202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION   (203, "Non-Authoritative Information"),
        NO_CONTENT                      (204, "No Content"),

        BAD_REQUEST                     (400, "Bad Request"),
        UNAUTHORIZED                    (401, "Unauthorized"),
        PAYMENT_REQUIRED                (402, "Payment Required"),
        FORBIDDEN                       (403, "Forbidden"),
        NOT_FOUND                       (404, "Not Found"),
        METHOD_NOT_ALLOWED              (405, "Method Not Allowed"),

        INTERNAL_SERVER_ERROR           (500, "Internal Server Error"),
        NOT_IMPLEMENTED                 (501, "Not Implemented");

        public final int code;
        public final String reasonPhrase;

        StatusCode(int code, String reasonPhrase) {
            this.code = code;
            this.reasonPhrase = reasonPhrase;
        }

        @Override
        public String toString() { 
            return code + " - " + reasonPhrase; 
        }
    }

    public StatusCode statusCode;

    public Response(StatusCode code) { 
        this.statusCode = code; 
    }

    public Response(StatusCode code, String message) {
        super(message);
        this.statusCode = code;
    }

    public Response(StatusCode code, Object body) {
        super(body);
        this.statusCode = code;
    }
}
