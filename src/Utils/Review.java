package Utils;

import java.time.LocalDateTime;

public class Review {
    int rate;
    int positionRate;
    int cleanRate;
    int serviceRate;
    int priceRate;
    String usernameCreator; 
    LocalDateTime dateCreation;

    Review(int rate,
           int positionRate,
           int cleanRate,
           int serviceRate,
           int priceRate,
           String usernameCreator,
           LocalDateTime dateCreation) 
    {
        this.rate = rate;
        this.positionRate = positionRate;
        this.cleanRate = cleanRate;
        this.serviceRate = serviceRate;
        this.priceRate = priceRate;
        this.usernameCreator = usernameCreator;
        this.dateCreation = dateCreation;
    }
}
