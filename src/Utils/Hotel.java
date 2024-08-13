package Utils;

import java.util.ArrayList;
import java.util.Map;

public class Hotel {
    private int id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private ArrayList<String> services;
    private int rate;
    private Map<String, Integer> ratings;

    private final Object lockRate = new Object();

    public Hotel(int id,
                 String name,
                 String description,
                 String city,
                 String phone,
                 ArrayList<String> services,
                 int rate,
                 Map<String, Integer> ratings) 
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.phone = phone;
        this.services = services;
        this.rate = rate;
        this.ratings = ratings;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public String getCity() { return this.city; }

    public int getRate() { 
        synchronized(this.lockRate) {
            return this.rate; 
        }
    }

    public Map<String, Integer> getRatings() {
        synchronized(this.ratings) {
            return this.ratings;
        }
    }

    public void setRate(int rate) {
        synchronized(this.ratings) {
            this.rate = (this.rate + rate) / 2;
        }
    }

    public void setCleaningRate(int rate) {
        synchronized (this.ratings) {
            this.setRatings("cleaning", rate);
        }
    }

    public void setPositionRate(int rate) {
        synchronized (this.ratings) {
            this.setRatings("position", rate);
        }
    }

    public void setServicesRate(int rate) {
        synchronized (this.ratings) {
            this.setRatings("services", rate);
        }
    }

    public void setQualityRate(int rate) {
        synchronized (this.ratings) {
            this.setRatings("quality", rate);
        }
    }

    private void setRatings(String name, int rate) {
        this.ratings.put(name, (this.ratings.get(name) + rate) / 2);
    }

    public String toString() {
        return "Name: " + this.name + 
                "\nCity: " + this.city + 
                "\nDescription: " + this.description + 
                "\nPhone: " + this.phone + 
                "\nServices: " + this.services.toString() + 
                "\nRate: " + this.getRate() + 
                "\nRatings: " + this.getRatings().toString();
    }
}
