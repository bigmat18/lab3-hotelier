package Framework.Database;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.google.gson.Gson;

import Data.Hotel;
import Data.Review;
import Data.User;

public class Database {
    private static File usersFile;

    private static Map<String, Table> tables = new HashMap<>();
    private static Gson gson = new Gson();
    private static boolean isInit = false;

    public static void inizialize() {
        isInit = true;
    }

    public static void shutdown() throws IOException, DatabaseInizializeException 
    {
        if(!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        for (Map.Entry<String, Table> table : tables.entrySet()) {
            table.getValue().unloadData(gson);
        }
    }

    public static <T> void addTable(Class<T> table) throws IOException, DatabaseInizializeException 
    {
        if (isInit)
            throw new DatabaseInizializeException("You shoudn't add more table after inizialization");
            
        addTable(table, table.getSimpleName() + "s.json");
    }

    public static <T> void addTable(Class<T> tableName, String fileName) throws IOException, DatabaseInizializeException 
    {
        if (isInit)
            throw new DatabaseInizializeException("You shoudn't add more table after inizialization");

        Table<T> table = new Table<>(tableName, fileName, gson);
        tables.put(tableName.getSimpleName(), table);
    }
    
    public static <T> boolean delete(Class<T> tableName, Predicate<T> predicate) throws DatabaseInizializeException
    {
        Table<T> table = tables.get(tableName.getSimpleName());
        if(table == null)
            return false;
        return table.delete(predicate);
    }
    
    public static <T> ArrayList<T> select(Class<T> tableName, Predicate<T> predicate) throws DatabaseInizializeException 
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null)
            return null;

        return table.select(predicate);
    }

    public static <T> T insert(Class<T> typeClass, Object... args) throws Exception
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        T instance = instanceTableElement(typeClass, args);
        return insert(typeClass, instance);
    }
    
    public static <T> T insert(Class<T> typeClass, T instance) throws Exception 
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");


        Table<T> table = tables.get(typeClass.getSimpleName());
        if (table == null || !table.insert(instance))
            return null;

        return instance;
    }
    
    private static <T> T instanceTableElement(Class<T> typeClass, Object... args) throws Exception {
        Class<?>[] parameter = new Class[args.length];
    
        for (int i = 0; i < parameter.length; i++) {
            if (args[i] instanceof Integer)
                parameter[i] = int.class;
            else
                parameter[i] = args[i].getClass();
        }
            
    
        Constructor<T> constructor = typeClass.getConstructor(parameter);
        T instance = constructor.newInstance(args);
        return instance;
    }

    public static <T> boolean sort(Class<T> typeClass, Comparator<T> compare) throws DatabaseInizializeException {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        Table<T> table = tables.get(typeClass.getSimpleName());
        if(table == null)
            return false;
        
        table.sort(compare);
        return true;
    }
}
