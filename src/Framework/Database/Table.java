package Framework.Database;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    // Nome del file associato alla tabella
    private final String fileName;

    // Riferimento al file su disco
    private File file;

    // Lista degli elementi della tabella
    private ArrayList<T> elements;

    // Flag che indica se il file è stato creato
    private boolean isAlredyCreated;

    // Classe che rappresenta il tipo di dato nella tabella
    private Class<T> table;

    // Costruttore per creare una tabella senza file associato
    Table(Class<T> table) {
        this.fileName = null;
        this.elements = new ArrayList<>();
        this.table = table;
    }

    // Costruttore che prende anche Gson (non utilizzato esplicitamente qui)
    Table(Class<T> table, String fileName, Gson gson) throws IOException {
        this(table, fileName); // Chiama il costruttore con fileName
    }

    // Costruttore per creare una tabella associata a un file
    Table(Class<T> table, String fileName) throws IOException {
        this.fileName = fileName;
        this.elements = new ArrayList<>();
        this.table = table;
    }

    // Metodo per caricare i dati dal file JSON
    public void loadData(Gson gson, String path) throws IOException {
        // Crea un oggetto file in base al percorso e al nome del file
        this.file = new File(path + "/" + fileName);
        this.isAlredyCreated = !this.file.exists(); // Verifica se il file esiste già
        this.file.createNewFile(); // Crea un nuovo file se non esiste

        // Se il file è stato appena creato, non ci sono dati da caricare
        if (this.isAlredyCreated)
            return;

        // Carica i dati dal file JSON
        try (FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8)) {
            JsonArray jsonArrayOfItems = JsonParser.parseReader(reader).getAsJsonArray();

            // Deserializza gli elementi JSON e li aggiunge alla lista
            for (JsonElement element : jsonArrayOfItems) {
                T data = gson.fromJson(element, this.table);
                this.elements.add(data);
            }
        }
    }

    // Metodo per salvare i dati della tabella nel file JSON
    public void unloadData(Gson gson) throws IOException {
        synchronized (this.elements) { // Blocca l'accesso agli elementi
            try (FileWriter writer = new FileWriter(this.file)) {
                gson.toJson(this.elements, writer); // Serializza gli elementi in JSON
            }
        }
    }

    // Inserisce un nuovo elemento nella tabella
    public boolean insert(T element) {
        synchronized (this.elements) {
            return this.elements.add(element); // Aggiunge l'elemento alla lista
        }
    }

    // Elimina gli elementi che soddisfano una condizione (predicato)
    public boolean delete(Predicate<T> predicate) {
        synchronized (this.elements) {
            return this.elements.removeIf(predicate); // Rimuove gli elementi che soddisfano il predicato
        }
    }

    // Seleziona e restituisce gli elementi che soddisfano un predicato
    public ArrayList<T> select(Predicate<T> predicate) {
        synchronized (this.elements) {
            // Filtra gli elementi in base al predicato e restituisce una nuova lista
            return this.elements.stream()
                    .filter(predicate)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    // Ordina gli elementi della tabella utilizzando un comparatore
    public void sort(Comparator<T> compare) {
        synchronized (this.elements) {
            Collections.sort(this.elements, compare); // Ordina gli elementi
        }
    }

    // Verifica se la tabella è associata a un file
    public boolean isTableStored() {
        return this.fileName != null;
    }

    // Rappresentazione testuale della tabella (il nome del file senza estensione)
    @Override
    public String toString() {
        return this.fileName.replaceAll(".json", "");
    }
}
