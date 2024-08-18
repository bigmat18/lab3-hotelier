package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Data.Hotel;
import Data.Review;
import Data.User;
import Framework.Server.Server;
import Framework.Server.Router;
import Framework.Server.Endpoint;
import Framework.Database.Database;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        Database.addTable(User.class);
        Database.addTable(Hotel.class);
        Database.addTable(Review.class);
        Database.inizialize();

        
        Router.addEndpoint("/login", new Login());
        Router.addEndpoint("/registration", new Registration());
        Router.addEndpoint("/logout", new Logout());
        Router.addEndpoint("/hotels", new Hotels());
        Router.addEndpoint("/reviews", new Reviews());
        Router.addEndpoint("/badge", new Badge());
        Router.inizialize();
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown server");
                try {
                    Database.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread rankCalculator = new Thread(new Rank());
        rankCalculator.start();

        try (Server server = new Server(8080, 10)) {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
