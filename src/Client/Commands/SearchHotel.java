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

public class SearchHotel extends Command {

    public SearchHotel() {
        this.name = "searchHotel";
        this.description = "\t\tView hotel for name and city";
    }

    public void execution(DataInputStream input, DataOutputStream output, AppStatus status) throws IOException {
        String name = Keyboard.StringReader("name: ");
        String city = Keyboard.StringReader("city: ");

        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("city", city);

        Message.write(Request.class, output, new Request("/hotels", Request.Methods.GET, obj));

        Response response = Message.read(Response.class, input);
        if (response.statusCode != Response.StatusCode.OK)
            System.out.println(response.getBody().getAsJsonObject().get("message").getAsString());

        for (JsonElement element : response.getBody().getAsJsonArray()) {
            Hotel hotel = Message.getMessage(Hotel.class, element.getAsJsonObject().toString());
            System.out.println("---------------------------------------------");
            System.out.println(hotel.toString());
        }
    }
}
