package ClientApp.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonObject;

import ClientApp.AppStatus;
import ClientApp.Command;
import ClientApp.Keyboard;
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
            System.out.println("[Error] You must be logged");
            return;
        }
        
        String hotelCity = Keyboard.StringReader("city: ");
        String hotelName = Keyboard.StringReader("name: ");

        int rate = Keyboard.IntReader("Rate: ");
        int positionRate = Keyboard.IntReader("Position rate: ");
        int cleaningRate = Keyboard.IntReader("Cleaning rate: ");
        int servicesRate = Keyboard.IntReader("Service rate: ");
        int priceRate = Keyboard.IntReader("Price rate: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("hotelCity", hotelCity);
        obj.addProperty("hotelName", hotelName);
        obj.addProperty("token", status.getToken());

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
