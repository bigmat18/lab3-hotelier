package Server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Table<T> {
    private File file;
    private final String fileName;
    private TreeMap<Integer, T> map;
    private boolean isAlredyCreated;

    Table(Class<T> table, String fileName, Gson gson) throws IOException {
        this(table, fileName);
        if(!this.isAlredyCreated)
            this.loadData(table, gson);
    }

    Table(Class<T> table, String fileName) throws IOException{
        this.fileName = fileName;
        this.map = new TreeMap<>();

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
        try (FileWriter writer = new FileWriter(this.file)) {
            synchronized (this.map) {
                gson.toJson(map.values(), writer);
            }
        }
    }

    public int getLastId() {
        synchronized(this.map) {
            if(this.map.isEmpty())
                return 0;
            return this.map.lastKey();
        }
    }

    public boolean insert(T element) {
        synchronized(this.map) {
            if(this.map.put(this.map.size() + 1, element) == null)
                return false;
            return true;
        }
    }

    public boolean delete(Predicate<Map.Entry<Integer, T>> predicate) {
        synchronized(this.map) {
            return this.map.entrySet().removeIf(predicate);
        }
    }

    public ArrayList<T> select(Predicate<Map.Entry<Integer, T>> predicate) {
        synchronized(this.map) {
            return this.map.entrySet()
                           .stream()
                           .filter(predicate)
                           .map(Map.Entry::getValue)
                           .collect(Collectors.toCollection(ArrayList::new));
        }
    }
}
