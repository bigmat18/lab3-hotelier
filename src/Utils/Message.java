package Utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Message {
    protected static transient Gson gson = new Gson();
    protected String body;

    public Message(String body) {
        this.body = body;
    }

    public Message(Object body) {
        this.body = this.gson.toJson(body);
    }

    public static<T> T getMessage(Class<T> clazz, String str) {
        return gson.fromJson(str, clazz);
    }

    public String getString() {
        return gson.toJson(this);
    }

    public String getRowBody() {
        return this.body;
    }

    public <T> T getBody(Class<T> clazz) {
        return gson.fromJson(this.body, clazz);
    }

    public <T> T getBody(Type typeOfT) {
        return gson.fromJson(this.body, typeOfT);
    }
}
