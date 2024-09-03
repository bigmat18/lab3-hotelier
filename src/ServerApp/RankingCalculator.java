package ServerApp;

import java.io.IOException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Data.Hotel;
import Data.Review;
import Framework.Database.Database;
import Framework.Database.DatabaseInizializeException;
import Framework.Database.TableNoExistsException;
import Framework.Notify.DataTooLongException;
import Framework.Notify.NotifySender;

public class RankingCalculator {
    private NotifySender sender;
    private final Comparator<Hotel> comparator;
    private Map<String, Hotel> topLocalRank;

    public RankingCalculator(NotifySender sender) throws DatabaseInizializeException, TableNoExistsException { 
        this.sender = sender;
        this.topLocalRank = new HashMap<>();
        this.comparator = new Comparator<Hotel>() {
            public int compare(Hotel lhs, Hotel rhs) {
                if (lhs.rank < rhs.rank)        return 1;
                else if (lhs.rank > rhs.rank)   return -1;
                else                            return 0;
            }
        };
        Database.sort(Hotel.class, this.comparator);
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true);
        for(Hotel hotel : hotels)
            topLocalRank.putIfAbsent(hotel.getCity(), hotel);
    }

    public void calculateAndUpdate() 
        throws DatabaseInizializeException, DataTooLongException, IOException, TableNoExistsException
    {
        this.updateRanking();
        Database.sort(Hotel.class, this.comparator);
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true);

        for(Hotel hotel : hotels) {
            if(!this.topLocalRank.get(hotel.getCity()).getName().equals(hotel.getName()) && 
                this.topLocalRank.get(hotel.getCity()).rank < hotel.rank) 
            {
                System.out.println("Send notify");
                this.topLocalRank.put(hotel.getCity(), hotel);
                this.sender.sendNotify("In city " + hotel.getCity() + " the new top hotel is " + hotel.getName());
            }
        }
    }

    private void updateRanking() throws DatabaseInizializeException, TableNoExistsException {
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entity -> true);
        ArrayList<Integer> reviewNumber = new ArrayList<>(hotels.size());
        ArrayList<Float> avgRate = new ArrayList<>(hotels.size());

        int globalAvgRate = 0; // M
        int avgReviewNumber = 0; // C

        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            ArrayList<Review> reviews = Database.select(Review.class,  entity -> entity.getHotelCity().equals(hotel.getCity()) &&
                                                                                           entity.getHotelName().equals(hotel.getName()));

            float value = 0; // R
            float totalWight = 0;

            for (Review review : reviews) {
                long deltaTime = Duration.between(review.getDateCreation(), LocalDateTime.now()).toDays();
                float timeWeight = (float)Math.exp(-0.1 * deltaTime);

                value += timeWeight * ((review.getRate() * 0.5) +
                         (review.getPositionRate() + review.getCleaningRate() + review.getServiceRate() + review.getPriceRate()) * 0.125);
                
                totalWight += timeWeight;
            }
            float norm = reviews.size() * totalWight;
            value /= norm != 0 ? norm : 1;

            avgRate.add(i, value);
            reviewNumber.add(i, reviews.size());

            globalAvgRate += value;
            avgReviewNumber += reviews.size();
        }

        globalAvgRate /= hotels.size() != 0 ? hotels.size() : 1;
        avgReviewNumber /= hotels.size() != 0 ? hotels.size() : 1;

        for (int i = 0; i < hotels.size(); i++) {
            if(avgReviewNumber + reviewNumber.get(i) == 0)
                continue;

            hotels.get(i).rank = ((avgReviewNumber * globalAvgRate) + (avgRate.get(i) * reviewNumber.get(i)))
                                  / (avgReviewNumber + reviewNumber.get(i));
        }
    }
}
