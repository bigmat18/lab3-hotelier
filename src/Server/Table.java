package Server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Table<T> {
    private File file;
    private final String fileName;
    private Map<Integer, T> map;
    private boolean isAlredyCreated;

    Table(Class<T> table, String fileName, Gson gson) throws IOException {
        this(table, fileName);
        if(!this.isAlredyCreated)
            this.loadData(table, gson);
    }

    Table(Class<T> table, String fileName) throws IOException{
        this.fileName = fileName;
        this.map = new HashMap<>();

        this.file = new File(fileName);
        this.isAlredyCreated = !this.file.exists();
        this.file.createNewFile();
    }

    public String toString() {
        return this.fileName.replaceAll(".json", "");
    }

    public void loadData(Class<T> table, Gson gson) throws IOException{
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            JsonArray jsonArrayOfItems = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArrayOfItems) {
                int id = element.getAsJsonObject().get("id").getAsInt();
                T data = gson.fromJson(element, table);
                map.put(id, data);
            }
        }
    }

    public void unloadData(Gson gson) throws IOException{
        this.saveData(gson);
        this.map.clear();
    }
    
    public void saveData(Gson gson) throws IOException{
        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(map.values(), writer);
        }
    }
}
