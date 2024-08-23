package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Client.AppStatus;
import Client.Command;
import Client.Keyboard;
import Data.Hotel;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class InsertReview extends Command {

    public InsertReview() {
        this.name = "insertReview";
        this.description = "\t\tAdd a review for a hotel";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        if (!status.isLogged()) {
            System.out.println("You must be logged");
            return;
        }
        
        String hotelCity = Keyboard.StringReader("city: ");
        String hotelName = Keyboard.StringReader("name: ");

        int rate = Keyboard.IntReader("Rate: ");
        int positionRate = Keyboard.IntReader("position rate: ");
        int cleaningRate = Keyboard.IntReader("cleaning rate: ");
        int servicesRate = Keyboard.IntReader("service rate: ");
        int priceRate = Keyboard.IntReader("price rate: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("hotelCity", hotelCity);
        obj.addProperty("hotelName", hotelName);
        obj.addProperty("username", status.getUsername());

        obj.addProperty("rate", rate);
        obj.addProperty("positionRate", positionRate);
        obj.addProperty("cleaningRate", cleaningRate);
        obj.addProperty("servicesRate", servicesRate);
        obj.addProperty("priceRate", priceRate);

        Message.write(Request.class, output, new Request("/reviews", Request.Methods.POST, obj));

        Response response = Message.read(Response.class, input);
        System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

    }
}
