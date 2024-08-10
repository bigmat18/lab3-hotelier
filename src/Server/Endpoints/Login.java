package Server.Endpoints;

import java.util.ArrayList;

import Server.Database;
import Server.Endpoint;
import Utils.Message;
import Utils.Request;
import Utils.Response;
import Utils.User;

public class Login extends Endpoint{

    @Override
    public Response POST(Request request) {
        User data = request.getBody(User.class);
        ArrayList<User> users = Database.select(User.class, entry -> entry.getValue().getUsername().equals(data.getUsername()));

        if(users.isEmpty())
            return new Response(Response.StatusCode.BAD_REQUEST, "User not exists");
        
        if(!users.get(0).getPassword().equals(data.getPassword()))
            return new Response(Response.StatusCode.BAD_REQUEST, "Password incorrect");

        return new Response(Response.StatusCode.OK, "Login successfull");
    }
    
}
