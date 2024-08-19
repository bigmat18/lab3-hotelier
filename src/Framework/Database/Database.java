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
    
    public static <T> boolean delete(Class<T> tableName, Predicate<T> predicate)
        throws DatabaseInizializeException, TableNoExistsException
    {
        Table<T> table = tables.get(tableName.getSimpleName());
        if(table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + "don't exits");
        return table.delete(predicate);
    }
    
    public static <T> ArrayList<T> select(Class<T> tableName, Predicate<T> predicate) 
        throws DatabaseInizializeException, TableNoExistsException
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + "don't exits");

        return table.select(predicate);
    }

    public static <T> T insert(Class<T> tableName, Object... args) throws Exception
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        T instance = instanceTableElement(tableName, args);
        return insert(tableName, instance);
    }
    
    public static <T> T insert(Class<T> tableName, T instance) throws Exception 
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");


        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null || !table.insert(instance))
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + "don't exits");

        return instance;
    }
    
    private static <T> T instanceTableElement(Class<T> tableName, Object... args) throws Exception {
        Class<?>[] parameter = new Class[args.length];
    
        for (int i = 0; i < parameter.length; i++) {
            if (args[i] instanceof Integer)
                parameter[i] = int.class;
            else
                parameter[i] = args[i].getClass();
        }
    
        Constructor<T> constructor = tableName.getConstructor(parameter);
        T instance = constructor.newInstance(args);
        return instance;
    }

    public static <T> boolean sort(Class<T> tableName, Comparator<T> compare) 
        throws DatabaseInizializeException, TableNoExistsException 
    {
        if (!isInit)
            throw new DatabaseInizializeException("Databse must be inizialize before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if(table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + "don't exits");        
        table.sort(compare);
        return true;
    }
}
