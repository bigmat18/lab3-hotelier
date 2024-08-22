package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.Timer;
import java.util.TimerTask;
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
import Framework.Notify.NotifySender;

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

        try (Server server = new Server()) {

            RankingCalculator rank = new RankingCalculator(server.getNotifySender());
            Timer rankingTimer = new Timer();
            rankingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("Ranking update");
                        rank.calculateAndUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, server.DATA_UPDATE);
            
            Timer databaseTimer = new Timer();
            databaseTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("Database update");
                        Database.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, server.NOTIFY_UPDATE);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("Shutdown server");
                        databaseTimer.cancel();
                        rankingTimer.cancel();
                        Database.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
