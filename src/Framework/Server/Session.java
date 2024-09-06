package Framework.Server;

import java.util.Random;

public class Session {
    // Identificatore dell'utente/sessione
    private String identifier;
    // Token unico della sessione
    private String token;

    // Costruttore che accetta un identificatore e genera un token casuale
    Session(String identifier) {
        this.token = generateRandonToken(15); // Genera un token di 15 caratteri
        this.identifier = identifier;
    }

    // Metodo privato per generare un token casuale
    private String generateRandonToken(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Caratteri possibili nel token
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        // Genera una stringa casuale di lunghezza specificata
        while (salt.length() < length) {
            int index = rnd.nextInt(SALTCHARS.length()); // Seleziona un indice casuale
            salt.append(SALTCHARS.charAt(index)); // Aggiunge il carattere corrispondente
        }

        return salt.toString(); // Restituisce il token generato
    }

    // Restituisce l'identificatore della sessione
    public String getIdentifier() {
        return this.identifier;
    }

    // Restituisce il token della sessione
    public String getToken() {
        return this.token;
    }
}
