package Utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class Message {
    protected static transient Gson gson = new Gson();
    public String body;

    public static<T> T getMessage(Class<T> clazz, String str) {
        return gson.fromJson(str, clazz);
    }

    public String getString() {
        return gson.toJson(this);
    }

    public <T> T getBody(Class<T> clazz) {
        return gson.fromJson(this.body, clazz);
    }

    public <T> T getBody(Type typeOfT) {
        return gson.fromJson(this.body, typeOfT);
    }
}
