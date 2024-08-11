package Server.Endpoints;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Server.Database;
import Server.Endpoint;
import Utils.Hotel;
import Utils.Request;
import Utils.Response;

public class Hotels extends Endpoint {
    public Response GET(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        JsonElement name = data.get("name");
        JsonElement city = data.get("city");
        JsonElement ordering = data.get("ordering");

        ArrayList<Hotel> hotels = Database.select(Hotel.class, 
                                                  entry -> (name == null || entry.getValue().getName().equals(name.getAsString())) &&
                                                           (city == null || entry.getValue().getCity().equals(city.getAsString())));

        if(ordering != null && ordering.getAsBoolean())
            hotels.sort(Comparator.comparing(Hotel::getRate).reversed());
        return new Response(Response.StatusCode.OK, hotels);
    }
}
