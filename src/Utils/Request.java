package Utils;

import com.google.gson.JsonObject;

public class Request extends Message {
    public enum Methods {
        GET,
        POST,
        DELETE,
        PATCH
    }
    public String url;
    public Methods method;

    public Request(String url, Methods method) {
        super("{}");
        this.url = url;
        this.method = method;
    }

    public Request(String url, Methods method, Object body) {
        super(body);
        this.url = url;
        this.method = method;
    }

    public Request(String url, Methods method, JsonObject body) {
        super(body.toString());
        this.url = url;
        this.method = method;
    }
}
