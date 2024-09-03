package Framework.Server;

import java.util.ArrayList;

import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Database.TableNoExistsException;

public class Endpoint {

    public Response GET(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }
    
    public Response POST(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    public Response DELETE(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    public Response PATCH(Request request) {
        return new Response(Response.StatusCode.METHOD_NOT_ALLOWED, 
                            Response.StatusCode.METHOD_NOT_ALLOWED.toString());
    }

    protected static String createSession(String identifier) throws DatabaseInizializeException, TableNoExistsException {
        Session session = new Session(identifier);
        ArrayList<Session> sessions = Database.select(Session.class, entry -> (entry.getToken().equals(session.getToken()) || 
                                                                                         entry.getIdentifier().equals(entry.getIdentifier())));
        if(!sessions.isEmpty()) 
            return null;

        try {
            Database.insert(Session.class, session);
            return session.getToken();
        } catch (Exception e) {
            return null;
        }
    }

    protected static String checkSession(String token) throws DatabaseInizializeException, TableNoExistsException{
        ArrayList<Session> sessions = Database.select(Session.class, entry -> (entry.getToken().equals(token)));
        if(sessions.isEmpty())
            return null;
        return sessions.get(0).getIdentifier();
    }

    protected static boolean removeSession(String token) throws DatabaseInizializeException, TableNoExistsException {
        ArrayList<Session> sessions = Database.select(Session.class, entry -> (entry.getToken().equals(token)));
        if (sessions.isEmpty())
            return false;
        
        return Database.delete(Session.class, entry -> entry.getToken().equals(token));
    }
}
