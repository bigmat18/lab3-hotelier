package Data;

import java.security.KeyStore.Entry;
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
    private float rate;
    private Map<String, Float> ratings;
    private ArrayList<String> services;
    
    public transient double rank;

    public Hotel(int id,
                 String name,
                 String description,
                 String city,
                 String phone,
                 ArrayList<String> services,
                 float rate,
                 Map<String, Float> ratings) 
    {
        this.rank = 0.0;
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

    public synchronized float getRate() { return this.rate; }
    
    public synchronized void setRate(float rate) { 
        this.rate = (this.rate + rate) / (this.rate == 0 ? 1 : 2); 
    }
    public Map<String, Float> getRatings() { return this.ratings; }

    public void setCleaningRate(float rate) { this.setRatings("cleaning", rate); }

    public void setPositionRate(float rate) { this.setRatings("position", rate); }

    public void setServicesRate(float rate) { this.setRatings("services", rate); }

    public void setQualityRate(float rate) { this.setRatings("quality", rate); }

    private synchronized void setRatings(String name, float rate) {
        float value = this.ratings.get(name);
        this.ratings.put(name, (value + rate) / (value == 0 ? 1 : 2));
    }

    @Override
    public String toString() {
        String str =  "\n=======================================" +
                      "\nName: " + this.name + 
                      "\nCity: " + this.city + 
                      "\nDescription: " + this.description + 
                      "\nPhone: " + this.phone;
        
        str += "\nServices: ";
        for(String entry : this.services)
            str += entry + ", ";
                              
        str += "\nRate: " + this.getRate() + "\n";
        
        for(Map.Entry<String, Float> entry : this.getRatings().entrySet()) 
            str += "- " + entry.getKey() + ": " + entry.getValue() + "\n";
        
        return str;
    }
}
