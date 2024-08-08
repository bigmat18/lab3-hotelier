package Server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import Server.Tables.Hotel;
import Server.Tables.User;

public class Database {
    private static File usersFile;

    private static Map<String, Table> tables = new HashMap<>();
    private static Gson gson = new Gson();

    public static void inizialize() throws IOException{
        addTable(User.class, gson);
        addTable(Hotel.class, gson);
    }

    public static void shutdown() {
        for(Map.Entry<String, Table> table : tables.entrySet()) {
            try { 
                table.getValue().unloadData(gson);
            } catch (IOException e) {
                System.err.println("Error to unload data from table: " + table.getValue());
            }
        }
    }

    private static<T> void addTable(Class<T> table, Gson gson) throws IOException{
        addTable(table, gson, table.getSimpleName() + "s.json");
    }

    private static<T> void addTable(Class<T> element, Gson gson, String fileName) throws IOException{
        try {
            Table<T> table = new Table<>(element, fileName, gson);
            tables.put(element.getSimpleName(), table);
        } catch (IOException e){
            System.err.println("Error to load table: " + element.getSimpleName());
        }
    }

    public static<T> int getLastTableId(Class<T> table) {
        return tables.get(table.getSimpleName()).getLastId();
    }

    public static<T> boolean insert(Class<T> tableName, T element) {
        Table<T> table = tables.get(tableName.getSimpleName());
        return table.insert(element);
    }

    public static <T> boolean delete(Class<T> tableName, int id) {
        Table<T> table = tables.get(tableName.getSimpleName());
        return table.delete(id);
    }
}
