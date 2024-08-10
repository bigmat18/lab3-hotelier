package Utils;

public class Request extends Message {
    public enum Methods {
        GET,
        POST,
        DELETE,
        PATCH
    }
    public String url;
    public Methods method;

    public Request(String url, Methods method, Object body) {
        this.url = url;
        this.method = method;
        this.body = super.gson.toJson(body);
    }
}
