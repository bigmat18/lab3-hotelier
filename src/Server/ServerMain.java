package Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
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

        try (Server server = new Server()) {

            Database.setDataPath(server.DATA_DIR);
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

            RankingCalculator rank = new RankingCalculator(server.getNotifySender());
            Timer rankingTimer = new Timer();
            rankingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        rank.calculateAndUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, server.NOTIFY_UPDATE_TIMEOUT);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("[SERVER] Shutdown server.");
                        rankingTimer.cancel();
                        Database.shutdown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            server.run();
        } catch (BindException e) {
            System.out.println("[SERVER] La porta richiesta dal server è attualmente in uso da un altro processo.");
        } catch (FileNotFoundException e) {
            System.out.println("[SERVER] Il file di configuarazioni config.json non è stato trovato.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
