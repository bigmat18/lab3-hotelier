package Server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.google.gson.Gson;

import Utils.Hotel;
import Utils.User;

public class Database {
    private static File usersFile;

    private static Map<String, Table> tables = new HashMap<>();
    private static Gson gson = new Gson();

    public static void inizialize() throws IOException {
        addTable(User.class, gson);
        addTable(Hotel.class, gson);
    }

    public static void shutdown() {
        for (Map.Entry<String, Table> table : tables.entrySet()) {
            try {
                table.getValue().unloadData(gson);
            } catch (IOException e) {
                System.err.println("Error to unload data from table: " + table.getValue());
            }
        }
    }

    private static <T> void addTable(Class<T> table, Gson gson) throws IOException {
        addTable(table, gson, table.getSimpleName() + "s.json");
    }

    private static <T> void addTable(Class<T> tableName, Gson gson, String fileName) throws IOException {
        try {
            Table<T> table = new Table<>(tableName, fileName, gson);
            tables.put(tableName.getSimpleName(), table);
        } catch (IOException e) {
            System.err.println("Error to load table: " + tableName.getSimpleName());
        }
    }
    
    public static <T> boolean delete(Class<T> tableName, Predicate<Map.Entry<Integer, T>> predicate) {
        Table<T> table = tables.get(tableName.getSimpleName());
        return table.delete(predicate);
    }
    
    public static <T> ArrayList<T> select(Class<T> tableName, Predicate<Map.Entry<Integer, T>> predicate) {
        Table<T> table = tables.get(tableName.getSimpleName());
        return table.select(predicate);
    }

    public static <T> T insert(Class<T> typeClass, Object... args) throws Exception  {
        T instance = instanceTableElement(typeClass, args);
        return insert(typeClass, instance);
    }
    
    public static <T> T insert(Class<T> typeClass, T instance) throws Exception {
        Table<T> table = tables.get(typeClass.getSimpleName());
        if (!table.insert(instance))
            return null;
    
        return instance;
    }
    
    private static <T> T instanceTableElement(Class<T> typeClass, Object... args) throws Exception {
        Class<?>[] parameter = new Class[args.length + 1];
        parameter[0] = int.class;
    
        for (int i = 1; i < parameter.length; i++)
            parameter[i] = args[i - 1].getClass();
    
        Constructor<T> constructor = typeClass.getConstructor(parameter);
    
        Object[] newArgs = new Object[parameter.length];
        newArgs[0] = getNewID(typeClass);
    
        System.arraycopy(args, 0, newArgs, 1, args.length);
        T instance = constructor.newInstance(newArgs);
        return instance;
    }
    
    public static <T> int getNewID(Class<T> tableName) {
        return tables.get(tableName.getSimpleName()).getLastId() + 1;
    }
}
