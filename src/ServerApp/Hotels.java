package ServerApp;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Data.Hotel;
import Framework.Database.Database;
import Framework.Server.Endpoint;
import Framework.Server.Request;
import Framework.Server.Response;

public class Hotels extends Endpoint {
    public Response GET(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();
        JsonElement name = data.get("name");
        JsonElement city = data.get("city");
        JsonElement ordering = data.get("ordering");

        try {
            ArrayList<Hotel> hotels = Database.select(Hotel.class,
                                                      entry -> ((name == null || entry.getName().equals(name.getAsString())) &&
                                                                (city == null || entry.getCity().equals(city.getAsString()))));
            if(hotels.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "No hotels exits");

            if (ordering != null && ordering.getAsBoolean())
                hotels.sort(Comparator.comparing(Hotel::getRate).reversed());

            return new Response(Response.StatusCode.OK, hotels);
        } catch (Exception e) {
            return new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }
}
