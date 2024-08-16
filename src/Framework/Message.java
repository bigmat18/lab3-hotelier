package Framework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Message {
    protected static transient Gson gson = new Gson();
    protected String body = "{}";

    public Message() {}

    public Message(String msg) {
        JsonObject obj = new JsonObject();
        obj.addProperty("message", msg);
        this.body = obj.toString();
    }

    public Message(JsonObject body) {
        this.body = body.toString();
    }

    public Message(Object body) {
        this.body = this.gson.toJson(body);
    }

    public static <T> T getMessage(Class<T> clazz, String str) { return gson.fromJson(str, clazz); }

    public String getString() { return gson.toJson(this); }
    
    public String getRowBody() { return this.body; }
    
    public JsonElement getBody() { return gson.fromJson(this.body, JsonElement.class); }
    
    public <T> T getBody(Class<T> clazz) { return gson.fromJson(this.body, clazz); }
    
    public static <T extends Message> T read(Class<T> clazz, DataInputStream input) throws IOException { 
        int length = input.readInt();
        byte[] stringBytes = new byte[length];
        input.readFully(stringBytes);
        return getMessage(clazz, new String(stringBytes, StandardCharsets.UTF_8));
    }

    public static <T extends Message> void write(Class<T> clazz, DataOutputStream output, T data) throws IOException {
        byte[] bytesResponse = data.getString().getBytes();
        output.writeInt(bytesResponse.length);
        output.write(bytesResponse);
        output.flush();
    }
    
}
