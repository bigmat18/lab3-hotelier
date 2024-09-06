package Framework.Server;

import java.util.ArrayList;

import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Database.TableNoExistsException;

public class Endpoint {

    // Metodo di default per le richieste GET, che restituisce un errore "Metodo non
    // consentito"
    public Response GET(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED,
                Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    // Metodo di default per le richieste POST, che restituisce un errore "Metodo
    // non consentito"
    public Response POST(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED,
                Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    // Metodo di default per le richieste DELETE, che restituisce un errore "Metodo
    // non consentito"
    public Response DELETE(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED,
                Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    // Metodo di default per le richieste PATCH, che restituisce un errore "Metodo
    // non consentito"
    public Response PATCH(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED,
                Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    // Crea una sessione per un utente specificato dall'identificatore
    protected static String createSession(String identifier)
            throws DatabaseInizializeException, TableNoExistsException {
        // Crea un nuovo oggetto Session con l'identificatore
        Session session = new Session(identifier);

        // Seleziona le sessioni che hanno lo stesso token o identificatore
        ArrayList<Session> sessions = Database.select(Session.class,
                entry -> (entry.getToken().equals(session.getToken()) || entry.getIdentifier().equals(identifier)));

        // Se una sessione esiste già, restituisce null
        if (!sessions.isEmpty())
            return null;

        try {
            // Inserisce la nuova sessione nel database
            Database.insert(Session.class, session);
            return session.getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verifica se esiste una sessione con un token specifico e restituisce
    // l'identificatore associato
    protected static String checkSession(String token) throws DatabaseInizializeException, TableNoExistsException {
        // Seleziona le sessioni che corrispondono al token
        ArrayList<Session> sessions = Database.select(Session.class, entry -> (entry.getToken().equals(token)));

        // Se non ci sono sessioni con il token, restituisce null
        if (sessions.isEmpty())
            return null;

        // Restituisce l'identificatore della prima sessione trovata
        return sessions.get(0).getIdentifier();
    }

    // Rimuove una sessione dal database in base al token
    protected static boolean removeSession(String token) throws DatabaseInizializeException, TableNoExistsException {
        // Seleziona le sessioni che corrispondono al token
        ArrayList<Session> sessions = Database.select(Session.class, entry -> (entry.getToken().equals(token)));

        // Se non ci sono sessioni con il token, restituisce false
        if (sessions.isEmpty())
            return false;

        // Elimina la sessione corrispondente e restituisce true se è stata rimossa
        return Database.delete(Session.class, entry -> entry.getToken().equals(token));
    }
}
