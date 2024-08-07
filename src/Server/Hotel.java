package Server;

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
}
