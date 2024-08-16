package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Utils.Hotel;
import Utils.Request;
import Utils.Review;
import Utils.User;

public class ServerMain {
    private static boolean running = true;

    private static final int THREAD_POOL_NUM = 10;
    private static final int PORT = 8080;

    private static final int timeout = 5000;

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown...");
                try { Database.shutdown(); }
                catch(Exception e) { System.out.println(e.getLocalizedMessage()); }
            }
        });

        Thread updateHotelsRanking = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(timeout);
                        ArrayList<Hotel> hotels = Database.select(Hotel.class, entity -> true);
                        ArrayList<Integer> reviewNumber = new ArrayList<>(hotels.size());
                        ArrayList<Float> avgRate = new ArrayList<>(hotels.size());

                        int globalAvgRate = 0; // M
                        int avgReviewNumber = 0; // C

                        for (int i = 0; i < hotels.size(); i++) {
                            Hotel hotel = hotels.get(i);
                            ArrayList<Review> reviews = Database.select(Review.class,
                                    entity -> entity.getHotelCity() == hotel.getCity() &&
                                            entity.getHotelName() == hotel.getName());

                            float value = 0; // R
                            for (Review review : reviews)
                                value += (review.getRate() * 0.5) +
                                        (review.getPositionRate() * 0.125) +
                                        (review.getCleaningRate() * 0.125) +
                                        (review.getServiceRate() * 0.125) +
                                        (review.getPriceRate() * 0.125) *
                                                review.getDateCreation().getNano();

                            value /= reviews.size();
                            avgRate.add(i, value);
                            reviewNumber.add(i, reviews.size());

                            globalAvgRate += value;
                            avgReviewNumber += reviews.size();
                        }

                        globalAvgRate /= hotels.size();
                        avgReviewNumber /= hotels.size();

                        for (int i = 0; i < hotels.size(); i++)
                            hotels.get(
                                    i).rank = ((avgReviewNumber * globalAvgRate)
                                            + (avgRate.get(i) * reviewNumber.get(i)))
                                            / (avgReviewNumber + reviewNumber.get(i));

                        Database.sort(Hotel.class, new Comparator<Hotel>() {
                            @Override
                            public int compare(Hotel lhs, Hotel rhs) {
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if (lhs.rank < rhs.rank)
                                    return -1;
                                else if (lhs.rank > rhs.rank)
                                    return 1;
                                else
                                    return 0;
                            }
                        });

                        System.out.println(Database.select(Hotel.class, entry -> true).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try(ServerSocket server = new ServerSocket(PORT)) {
            
            ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_NUM);

            Router.inizialize();
            Database.inizialize();
            System.out.println(Database.select(Hotel.class, entry -> true).toString());

            updateHotelsRanking.start();
            
            while (running){
                Socket connection = server.accept();
                System.out.println("Connection opened with: " + connection.getInetAddress());
                pool.execute(new RequestHandler(connection));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
