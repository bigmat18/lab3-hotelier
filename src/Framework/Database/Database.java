package Framework.Database;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.google.gson.Gson;

public class Database {
    // Mappa che associa i nomi delle tabelle agli oggetti Table
    private static Map<String, Table> tables = new HashMap<>();

    // Oggetto Gson per serializzare e deserializzare i dati JSON
    private static Gson gson = new Gson();

    // Flag che indica se il database è stato inizializzato
    private static boolean isInit = false;

    // Percorso del file dei dati
    private static String dataPath;

    // Metodo per inizializzare il database
    public static void inizialize() throws IOException, DatabaseInizializeException {
        // Verifica se il database è già stato inizializzato
        if (isInit)
            throw new DatabaseInizializeException("Database is already initialized");

        isInit = true;

        // Carica i dati per ogni tabella memorizzata
        for (Map.Entry<String, Table> table : tables.entrySet()) {
            if (table.getValue().isTableStored())
                table.getValue().loadData(gson, dataPath);
        }
        System.out.println("[DATABASE] Initialization completed.");
    }

    // Metodo per chiudere il database
    public static void shutdown() throws IOException, DatabaseInizializeException {
        // Verifica se il database è stato inizializzato
        if (!isInit)
            throw new DatabaseInizializeException("Database must be initialized before");

        // Scarica i dati per ogni tabella memorizzata
        for (Map.Entry<String, Table> table : tables.entrySet()) {
            if (table.getValue().isTableStored())
                table.getValue().unloadData(gson);
        }
        System.out.println("[DATABASE] Shutdown completed.");
    }

    // Metodo per impostare il percorso del file dei dati
    public static void setDataPath(String path) {
        dataPath = path;
    }

    // Aggiunge una nuova tabella al database e crea un file JSON
    public static <T> void addTable(Class<T> table) throws IOException, DatabaseInizializeException {
        // Verifica che il database non sia stato inizializzato
        if (isInit)
            throw new DatabaseInizializeException("You shouldn't add more tables after initialization");

        addTable(table, table.getSimpleName() + "s.json");
    }

    // Aggiunge una tabella senza file associato
    public static <T> void addTableWithoutFile(Class<T> tableName) throws DatabaseInizializeException {
        if (isInit)
            throw new DatabaseInizializeException("You shouldn't add more tables after initialization");

        Table<T> table = new Table<>(tableName);
        tables.put(tableName.getSimpleName(), table);
    }

    // Aggiunge una nuova tabella specificando un file per la memorizzazione
    public static <T> void addTable(Class<T> tableName, String fileName)
            throws IOException, DatabaseInizializeException {
        if (isInit)
            throw new DatabaseInizializeException("You shouldn't add more tables after initialization");

        Table<T> table = new Table<>(tableName, fileName, gson);
        tables.put(tableName.getSimpleName(), table);
    }

    // Elimina un elemento dalla tabella specificata in base a un predicato
    public static <T> boolean delete(Class<T> tableName, Predicate<T> predicate)
            throws DatabaseInizializeException, TableNoExistsException {
        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + " doesn't exist");
        return table.delete(predicate);
    }

    // Seleziona gli elementi dalla tabella che soddisfano il predicato
    public static <T> ArrayList<T> select(Class<T> tableName, Predicate<T> predicate)
            throws DatabaseInizializeException, TableNoExistsException {
        if (!isInit)
            throw new DatabaseInizializeException("Database must be initialized before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + " doesn't exist");

        return table.select(predicate);
    }

    // Inserisce un nuovo elemento nella tabella specificata creando un'istanza
    public static <T> T insert(Class<T> tableName, Object... args) throws Exception {
        if (!isInit)
            throw new DatabaseInizializeException("Database must be initialized before");

        T instance = instanceTableElement(tableName, args);
        return insert(tableName, instance);
    }

    // Inserisce un'istanza già creata nella tabella specificata
    public static <T> T insert(Class<T> tableName, T instance) throws Exception {
        if (!isInit)
            throw new DatabaseInizializeException("Database must be initialized before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null || !table.insert(instance))
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + " doesn't exist");

        return instance;
    }

    // Crea una nuova istanza di un elemento della tabella in base ai parametri
    private static <T> T instanceTableElement(Class<T> tableName, Object... args) throws Exception {
        Class<?>[] parameter = new Class[args.length];

        // Mappa i tipi dei parametri
        for (int i = 0; i < parameter.length; i++) {
            if (args[i] instanceof Integer)
                parameter[i] = int.class;
            else
                parameter[i] = args[i].getClass();
        }

        // Ottiene il costruttore appropriato e crea l'istanza
        Constructor<T> constructor = tableName.getConstructor(parameter);
        T instance = constructor.newInstance(args);
        return instance;
    }

    // Ordina gli elementi della tabella specificata utilizzando un comparatore
    public static <T> boolean sort(Class<T> tableName, Comparator<T> compare)
            throws DatabaseInizializeException, TableNoExistsException {
        if (!isInit)
            throw new DatabaseInizializeException("Database must be initialized before");

        Table<T> table = tables.get(tableName.getSimpleName());
        if (table == null)
            throw new TableNoExistsException("Table " + tableName.getSimpleName() + " doesn't exist");

        table.sort(compare);
        return true;
    }
}
