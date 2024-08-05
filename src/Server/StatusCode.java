package Server;

public enum StatusCode {
    OK                              (200, "OK"),
    CREATED                         (201, "Created"),
    ACCEPTED                        (202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION   (203, "Non-Authoritative Information"),
    NO_CONTENT                      (204, "No Content"),
    RESET_CONTENT                   (205, "Reset Content"),
    PARTIAL_CONTENT                 (206, "Partial Content"),

    BAD_REQUEST                     (400, "Bad Request"),
    UNAUTHORIZED                    (401, "Unauthorized"),
    PAYMENT_REQUIRED                (402, "Payment Required"),
    FORBIDDEN                       (403, "Forbidden"),
    NOT_FOUND                       (404, "Not Found"),
    METHOD_NOT_ALLOWED              (405, "Method Not Allowed");

    private final int code;
    private final String reasonPhrase;

    StatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() { return code; }

    public String getReasonPhrase() { return reasonPhrase; }

    @Override
    public String toString() { return code + " " + reasonPhrase; }
}