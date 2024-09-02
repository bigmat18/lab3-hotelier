package ClientApp.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ClientApp.AppStatus;
import ClientApp.Command;
import ClientApp.Keyboard;
import Data.Hotel;
import Framework.Server.Message;
import Framework.Server.Request;
import Framework.Server.Response;

public class SearchHotel extends Command {

    public SearchHotel() {
        this.name = "searchHotel";
        this.description = "\t\tView hotel for name and city";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        String city = Keyboard.StringReader("city: ");
        String name = Keyboard.StringReader("name: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("city", city);

        Message.write(Request.class, output, new Request("/hotels", Request.Methods.GET, obj));
        Response response = Message.read(Response.class, input);

        if (response.statusCode != Response.StatusCode.OK)
            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());
        else {
            for (JsonElement element : response.getBody().getAsJsonArray()) {
                Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
                System.out.println("---------------------------------------------");
                System.out.println(hotel.toString());
            }
        }
    }
}
