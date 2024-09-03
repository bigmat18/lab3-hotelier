package Framework.Database;

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
    private final String fileName;
    private File file;
    private ArrayList<T> elements;
    private boolean isAlredyCreated;
    private Class<T> table;
    
    Table(Class<T> table) {
        this.fileName = null;
        this.elements = new ArrayList<>();
        this.table = table;
    }

    Table(Class<T> table, String fileName, Gson gson) throws IOException {
        this(table, fileName);
    }

    Table(Class<T> table, String fileName) throws IOException{
        this.fileName = fileName;
        this.elements = new ArrayList<>();
        this.table = table;
    }
    
    public void loadData(Gson gson, String path) throws IOException{
        this.file = new File(path + fileName);
        this.isAlredyCreated = !this.file.exists();
        this.file.createNewFile();
        
        if(this.isAlredyCreated)
            return;

        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            JsonArray jsonArrayOfItems = JsonParser.parseReader(reader).getAsJsonArray();
            
            for (JsonElement element : jsonArrayOfItems) {
                T data = gson.fromJson(element, this.table);
                this.elements.add(data);
            }
        }
    }
    
    public void unloadData(Gson gson) throws IOException{
        synchronized (this.elements) {
            try (FileWriter writer = new FileWriter(this.file)) {
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
        synchronized(this.elements) {
            Collections.sort(this.elements, compare);
        }
    }

    public boolean isTableStored() { return this.fileName != null; }
    
    @Override
    public String toString() {
        return this.fileName.replaceAll(".json", "");
    }
}
