package Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Database {
    private static final String HOTELS_FILE_NAME = "Hotels.json";
    private static File file;

    public static void inizialize() throws IOException {
        file = new File(HOTELS_FILE_NAME);

        try(FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {

            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArrayOfItems = jsonElement.getAsJsonArray();

            for(JsonElement element : jsonArrayOfItems) {
                JsonObject itemJsonObject = element.getAsJsonObject();
                System.out.println(itemJsonObject.get("name").toString());
            }
        }
    }

    public static void shutdown() {}
}
