package Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private int rate;
    private int positionRate;
    private int cleaningRate;
    private int serviceRate;
    private int priceRate;

    private String usernameCreator; 
    private String dateCreation;

    private String hotelName;
    private String hotelCity;

    public Review(int rate,
           int positionRate,
           int cleaningRate,
           int serviceRate,
           int priceRate,
           String usernameCreator,
           String hotelName, 
           String hotelCity) 
    {
        this.rate = rate;
        this.positionRate = positionRate;
        this.cleaningRate = cleaningRate;
        this.serviceRate = serviceRate;
        this.priceRate = priceRate;
        this.usernameCreator = usernameCreator;
        this.dateCreation = LocalDateTime.now().format(formatter);
        
        this.hotelName = hotelName;
        this.hotelCity = hotelCity;
    }

    public String getHotelName() { return this.hotelName; }

    public String getHotelCity() { return this.hotelCity; }

    public int getRate() { return this.rate; }

    public int getPositionRate() { return this.positionRate; }

    public int getCleaningRate() { return this.cleaningRate; }

    public int getServiceRate() { return this.serviceRate; }

    public int getPriceRate() { return this.priceRate; }

    public LocalDateTime getDateCreation() { return LocalDateTime.parse(this.dateCreation, formatter); }
}
