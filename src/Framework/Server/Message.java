package Framework.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Message {
    // Gson è usato per la serializzazione e deserializzazione JSON
    protected static transient Gson gson = new Gson();
    // Stringa che rappresenta il corpo del messaggio, di default un JSON vuoto
    protected String body = "{}";

    // Costruttore vuoto, inizializza un messaggio con un corpo vuoto
    public Message() {
    }

    // Costruttore che accetta una stringa come messaggio
    public Message(String msg) {
        // Crea un JsonObject con una proprietà "message" che contiene la stringa
        // passata
        JsonObject obj = new JsonObject();
        obj.addProperty("message", msg);
        this.body = obj.toString();
    }

    // Costruttore che accetta un JsonObject come corpo del messaggio
    public Message(JsonObject body) {
        this.body = body.toString();
    }

    // Costruttore che accetta un oggetto generico e lo serializza in JSON
    public Message(Object body) {
        this.body = gson.toJson(body);
    }

    // Metodo statico che deserializza una stringa JSON in un oggetto della classe
    // specificata
    public static <T> T getMessage(Class<T> clazz, String str) {
        return gson.fromJson(str, clazz);
    }

    // Metodo statico che legge un messaggio da un DataInputStream e lo deserializza
    public static <T extends Message> T read(Class<T> clazz, DataInputStream input) throws IOException {
        int length = input.readInt(); // Legge la lunghezza del messaggio
        byte[] stringBytes = new byte[length]; // Alloca un array di byte della lunghezza specificata
        input.readFully(stringBytes); // Legge il contenuto del messaggio nel buffer
        return getMessage(clazz, new String(stringBytes, StandardCharsets.UTF_8)); // Deserializza il messaggio
    }

    // Metodo statico che scrive un messaggio su un DataOutputStream serializzandolo
    public static <T extends Message> void write(Class<T> clazz, DataOutputStream output, T data) throws IOException {
        byte[] bytesResponse = data.getJsonString().getBytes(); // Ottiene la rappresentazione JSON del messaggio
        output.writeInt(bytesResponse.length); // Scrive la lunghezza del messaggio
        output.write(bytesResponse); // Scrive il contenuto del messaggio
        output.flush(); // Assicura che tutti i dati siano stati scritti
    }

    // Restituisce la rappresentazione JSON dell'oggetto Message
    public String getJsonString() {
        return gson.toJson(this);
    }

    // Restituisce il corpo grezzo del messaggio come stringa
    public String getRowBody() {
        return this.body;
    }

    // Restituisce il corpo del messaggio come JsonElement (utile per manipolazioni
    // JSON)
    public JsonElement getBody() {
        return gson.fromJson(this.body, JsonElement.class);
    }
}
