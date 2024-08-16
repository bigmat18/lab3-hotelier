package Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Hotel {
    private int id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private ArrayList<String> services;
    private Map<String, Float> ratings;
    
    private float rate;
    private final Object lockRate = new Object();

    public transient float rank = 0;

    public Hotel(int id,
                 String name,
                 String description,
                 String city,
                 String phone,
                 ArrayList<String> services,
                 float rate,
                 Map<String, Float> ratings) 
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

    public String getName() { return this.name; }

    public String getCity() { return this.city; }

    public synchronized float getRate() { 
        return this.rate; 
    }
    
    public synchronized void setRate(float rate) { 
        this.rate = (this.rate + rate) / (this.rate == 0 ? 1 : 2); 
    }

    public Map<String, Float> getRatings() {
        synchronized(this.ratings) {
            return this.ratings;
        }
    }

    public void setCleaningRate(float rate) {
        synchronized (this.ratings) {
            this.setRatings("cleaning", rate);
        }
    }

    public void setPositionRate(float rate) {
        synchronized (this.ratings) {
            this.setRatings("position", rate);
        }
    }

    public void setServicesRate(float rate) {
        synchronized (this.ratings) {
            this.setRatings("services", rate);
        }
    }

    public void setQualityRate(float rate) {
        synchronized (this.ratings) {
            this.setRatings("quality", rate);
        }
    }

    private void setRatings(String name, float rate) {
        float value = this.ratings.get(name);
        this.ratings.put(name, (value + rate) / (value == 0 ? 1 : 2));
    }

    public String toString() {
        return  "Rank: " + this.rank +
                "\nName: " + this.name + 
                "\nCity: " + this.city + 
                "\nDescription: " + this.description + 
                "\nPhone: " + this.phone + 
                "\nServices: " + this.services.toString() + 
                "\nRate: " + this.getRate() + 
                "\nRatings: " + this.getRatings().toString();
    }
}
