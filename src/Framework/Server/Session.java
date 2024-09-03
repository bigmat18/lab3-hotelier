package Framework.Server;

import java.util.Random;

public class Session {
    private String identifier;
    private String token;

    Session(String identifier) {
        this.token = generateRandonToken(15);
        this.identifier = identifier;
    }

    private String generateRandonToken(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = rnd.nextInt(SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public String getIdentifier() { return this.identifier; }

    public String getToken() { return this.token; }
}
