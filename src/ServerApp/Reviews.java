package ServerApp;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import Data.Hotel;
import Data.Review;
import Data.User;

import Framework.Database.Database;
import Framework.Server.Endpoint;;
import Framework.Server.Request;
import Framework.Server.Response;

public class Reviews extends Endpoint {

    public Response POST(Request request) {
        JsonObject data = request.getBody().getAsJsonObject();

        if(!dataAreValid(data))
            return new Response(Response.StatusCode.BAD_REQUEST, "Value in rates must be between 0 and 5");
            
        try {
            String username = data.get("username").getAsString();
            ArrayList<User> users = Database.select(User.class, entry -> entry.getUsername().equals(username));
            if (users.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "User doesn't exits");

            String hotelName = data.get("hotelName").getAsString();
            String hotelCity = data.get("hotelCity").getAsString();

            ArrayList<Hotel> hotels = Database.select(Hotel.class, entry-> entry.getCity().equals(hotelCity) && entry.getName().equals(hotelName));
            if(hotels.isEmpty())
                return new Response(Response.StatusCode.BAD_REQUEST, "Hotel doesn't exits");

            Database.insert(Review.class, data.get("rate").getAsInt(),
                                                    data.get("positionRate").getAsInt(),
                                                    data.get("cleaningRate").getAsInt(),
                                                    data.get("servicesRate").getAsInt(),
                                                    data.get("priceRate").getAsInt(),
                                                    username,
                                                    hotelName,
                                                    hotelCity);

            hotels.get(0).setRate(data.get("rate").getAsInt());
            hotels.get(0).setPositionRate(data.get("positionRate").getAsInt());
            hotels.get(0).setCleaningRate(data.get("cleaningRate").getAsInt());
            hotels.get(0).setServicesRate(data.get("servicesRate").getAsInt());
            hotels.get(0).setServicesRate(data.get("priceRate").getAsInt());

            users.get(0).incrementLevel();
            return new Response(Response.StatusCode.CREATED, "Review added");
        } catch(Exception e) {
            e.printStackTrace();
            return new Response(Response.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private boolean dataAreValid(JsonObject data) {
        int rate = data.get("rate").getAsInt();
        if(rate > 5 || rate < 0)
            return false;

        int positionRate = data.get("positionRate").getAsInt();
        if(positionRate > 5 || positionRate < 0)
            return false;

        int cleaningRate = data.get("cleaningRate").getAsInt();
        if(cleaningRate > 5 || cleaningRate < 0)
            return false;

        int serviceRate = data.get("servicesRate").getAsInt();
        if(serviceRate > 5 || serviceRate < 0)
            return false;

        return true;
    }
}
