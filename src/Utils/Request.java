package Utils;

import java.io.Serializable;

public class Request implements Serializable {
    public enum Methods {
        GET,
        POST,
        DELETE,
        PATCH
    }

    private static final long serialVersionUID = 1L;
    public String url;
    public Methods method;


    public Request(String url, Methods method) {
        this.url = url;
        this.method = method;
    }
}
