package Framework;

import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.crypto.Data;

import Data.Hotel;
import Data.Review;

public class Rank implements Runnable {
    private boolean running = true;
    private final int timeout = 5000;

    private final Comparator<Hotel> comparator = new Comparator<Hotel>() {
        @Override
        public int compare(Hotel lhs, Hotel rhs) {
            if (lhs.rank < rhs.rank)        return -1;
            else if (lhs.rank > rhs.rank)   return 1;
            else                            return 0;
        }
    };

    @Override
    public void run() {
        while (true) {
            try {
                while(true) {
                    Thread.sleep(this.timeout);
                    Database.sort(Hotel.class, this.comparator);
                    System.out.println(Database.select(Hotel.class, entry -> true));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRanking() {
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
    }
}
