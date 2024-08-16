package Framework;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    private ArrayList<T> elements;
    private boolean isAlredyCreated;

    Table(Class<T> table, String fileName, Gson gson) throws IOException {
        this(table, fileName);
        if(!this.isAlredyCreated)
            this.loadData(table, gson);
    }

    Table(Class<T> table, String fileName) throws IOException{
        this.fileName = fileName;
        this.elements = new ArrayList<>();

        this.file = new File(fileName);
        this.isAlredyCreated = !this.file.exists();
        this.file.createNewFile();
    }
    
    public void loadData(Class<T> table, Gson gson) throws IOException{
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            JsonArray jsonArrayOfItems = JsonParser.parseReader(reader).getAsJsonArray();
            
            for (JsonElement element : jsonArrayOfItems) {
                T data = gson.fromJson(element, table);
                this.elements.add(data);
            }
        }
    }
    
    public void unloadData(Gson gson) throws IOException{
        try (FileWriter writer = new FileWriter(this.file)) {
            synchronized (this.elements) {
                gson.toJson(this.elements, writer);
            }
        }
    }
    
    public boolean insert(T element) {
        synchronized(this.elements) {
            return this.elements.add(element);
        }
    }
    
    public boolean delete(Predicate<T> predicate) {
        synchronized(this.elements) {
            return this.elements.removeIf(predicate);
        }
    }
    
    public ArrayList<T> select(Predicate<T> predicate) {
        synchronized(this.elements) {
            return this.elements.stream()
            .filter(predicate)
            .collect(Collectors.toCollection(ArrayList::new));
        }
    }
    
    public void sort(Comparator<T> compare) {
        Collections.sort(this.elements, compare);
    }
    
    @Override
    public String toString() {
        return this.fileName.replaceAll(".json", "");
    }
}
