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
    // Dichiarazione delle variabili d'istanza
    private NotifySender sender; // Oggetto utilizzato per inviare notifiche
    private final Comparator<Hotel> comparator; // Comparatore per ordinare gli hotel in base al ranking
    private Map<String, Hotel> topLocalRank; // Mappa che memorizza l'hotel con il punteggio più alto per città

    // Costruttore della classe RankingCalculator
    public RankingCalculator(NotifySender sender) throws DatabaseInizializeException, TableNoExistsException {
        this.sender = sender; // Assegna l'oggetto NotifySender passato come argomento
        this.topLocalRank = new HashMap<>(); // Inizializza la mappa per il ranking locale
        // Inizializza il comparatore per ordinare gli hotel
        this.comparator = new Comparator<Hotel>() {
            public int compare(Hotel lhs, Hotel rhs) {
                // Ordinamento decrescente per rank
                if (lhs.rank < rhs.rank)
                    return 1;
                else if (lhs.rank > rhs.rank)
                    return -1;
                else
                    return 0;
            }
        };
        // Ordina gli hotel nel database utilizzando il comparatore
        Database.sort(Hotel.class, this.comparator);
        // Seleziona tutti gli hotel dal database
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true);
        // Inserisce nella mappa l'hotel con il miglior punteggio per ciascuna città
        for (Hotel hotel : hotels)
            topLocalRank.putIfAbsent(hotel.getCity(), hotel);
    }

    // Metodo che calcola e aggiorna il ranking degli hotel
    public void calculateAndUpdate()
            throws DatabaseInizializeException, DataTooLongException, IOException, TableNoExistsException {
        this.updateRanking(); // Aggiorna il ranking degli hotel
        Database.sort(Hotel.class, this.comparator); // Riordina gli hotel dopo l'aggiornamento
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entry -> true); // Seleziona nuovamente tutti gli hotel

        // Cicla su tutti gli hotel
        for (Hotel hotel : hotels) {
            // Verifica se c'è un nuovo hotel con un rank più alto rispetto al precedente
            // nella stessa città
            if (!this.topLocalRank.get(hotel.getCity()).getName().equals(hotel.getName()) &&
                    this.topLocalRank.get(hotel.getCity()).rank < hotel.rank) {
                System.out.println("Send notify"); // Stampa un messaggio di debug
                this.topLocalRank.put(hotel.getCity(), hotel); // Aggiorna la mappa con il nuovo miglior hotel
                // Invia una notifica tramite NotifySender
                this.sender.sendNotify("In city " + hotel.getCity() + " the new top hotel is " + hotel.getName());
            }
        }
    }

    // Metodo privato che aggiorna il ranking di ciascun hotel
    private void updateRanking() throws DatabaseInizializeException, TableNoExistsException {
        ArrayList<Hotel> hotels = Database.select(Hotel.class, entity -> true); // Seleziona tutti gli hotel
        ArrayList<Integer> reviewNumber = new ArrayList<>(hotels.size()); // Numero di recensioni per ogni hotel
        ArrayList<Float> avgRate = new ArrayList<>(hotels.size()); // Valutazione media per ogni hotel

        int globalAvgRate = 0; // Media globale delle valutazioni (M)
        int avgReviewNumber = 0; // Numero medio di recensioni (C)

        // Cicla su tutti gli hotel per calcolare il ranking
        for (int i = 0; i < hotels.size(); i++) {
            Hotel hotel = hotels.get(i);
            // Seleziona le recensioni dell'hotel corrente dal database
            ArrayList<Review> reviews = Database.select(Review.class,
                    entity -> entity.getHotelCity().equals(hotel.getCity()) &&
                            entity.getHotelName().equals(hotel.getName()));

            float value = 0; // Valore del punteggio (R)
            float totalWeight = 0; // Peso totale delle recensioni

            // Cicla su tutte le recensioni per calcolare il punteggio ponderato
            for (Review review : reviews) {
                long deltaTime = Duration.between(review.getDateCreation(), LocalDateTime.now()).toDays();
                float timeWeight = (float) Math.exp(-0.1 * deltaTime); // Ponderazione temporale

                // Calcolo del punteggio ponderato per ogni recensione
                value += timeWeight * ((review.getRate() * 0.5) +
                        (review.getPositionRate() + review.getCleaningRate() + review.getServiceRate()
                                + review.getPriceRate()) * 0.125);

                totalWeight += timeWeight; // Aggiunge il peso temporale totale
            }
            float norm = reviews.size() * totalWeight; // Normalizzazione
            value /= norm != 0 ? norm : 1; // Evita la divisione per zero

            avgRate.add(i, value); // Aggiunge il valore medio del punteggio
            reviewNumber.add(i, reviews.size()); // Aggiunge il numero di recensioni

            globalAvgRate += value; // Somma il punteggio globale
            avgReviewNumber += reviews.size(); // Somma il numero totale di recensioni
        }

        // Calcolo delle medie globali
        globalAvgRate /= hotels.size() != 0 ? hotels.size() : 1;
        avgReviewNumber /= hotels.size() != 0 ? hotels.size() : 1;

        // Aggiorna il rank di ciascun hotel in base alle medie e ai punteggi calcolati
        for (int i = 0; i < hotels.size(); i++) {
            if (avgReviewNumber + reviewNumber.get(i) == 0)
                continue;

            // Formula per aggiornare il ranking dell'hotel
            hotels.get(i).rank = ((avgReviewNumber * globalAvgRate) + (avgRate.get(i) * reviewNumber.get(i)))
                    / (avgReviewNumber + reviewNumber.get(i));
        }
    }
}
