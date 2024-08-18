package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Data.Hotel;
import Data.Review;
import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Notify.DataTooLongException;
import Framework.Notify.NotifySender;

public class RankingCalculator {
    private NotifySender sender;
    private final Comparator<Hotel> comparator;
    private Map<String, Hotel> topLocalRank;

    public RankingCalculator(NotifySender sender) throws DatabaseInizializeException { 
        this.sender = sender;
        this.topLocalRank = new HashMap<>();
        this.comparator = new Comparator<Hotel>() {
            public int compare(Hotel lhs, Hotel rhs) {
                if (lhs.rank < rhs.rank)        return -1;
                else if (lhs.rank > rhs.rank)   return 1;
                else                            return 0;
            }
        };
        Database.sort(Hotel.class, this.comparator);
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true);
        for(Hotel hotel : hotels)
            topLocalRank.putIfAbsent(hotel.getCity(), hotel);
    }

    public void calculateAndUpdate() 
        throws DatabaseInizializeException, DataTooLongException, IOException 
    {
        this.updateRanking();
        Database.sort(Hotel.class, this.comparator);
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true);
        for(Hotel hotel : hotels) {
            if(!this.topLocalRank.get(hotel.getCity()).getName().equals(hotel.getName()) && this.topLocalRank
                    .get(hotel.getCity()).rank < hotel.rank) {
                this.sender.sendNotify("In city " + hotel.getCity() + " the new top hotel is " + hotel.getName());
                this.topLocalRank.put(hotel.getCity(), hotel);
            }
        }
    }

    private void updateRanking() throws DatabaseInizializeException {
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
            hotels.get(i).rank = ((avgReviewNumber * globalAvgRate)
                                + (avgRate.get(i) * reviewNumber.get(i)))
                                / (avgReviewNumber + reviewNumber.get(i));
    }
}
