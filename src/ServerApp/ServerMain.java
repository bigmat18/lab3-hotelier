package ServerApp;

import java.io.FileNotFoundException;
import java.net.BindException;
import java.util.Timer;
import java.util.TimerTask;

import Data.Hotel;
import Data.Review;
import Data.User;
import Framework.Server.Server;
import Framework.Server.Router;
import Framework.Database.Database;

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
            }, 0, server.NOTIFY_TIMEOUT);

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
            System.out.println("[SERVER] The port requested by the server is currently in use by another process.");
        } catch (FileNotFoundException e) {
            System.out.println("[SERVER] File config.json doesn't exsits");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
