package Data;

public class User{
    private static final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    
    private String password;
    private String username;
    private int level = 0;

    public User(String password, String username) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() { return this.password; }

    public String getUsername() { return this.username; }

    public String getBandge() {
        switch (this.level) {
            case 0:
                return "Recensore";
            case 1:
                return "Recensore esperto";
            case 2:
                return "Contributore";
            case 3:
                return "Contributore esperto";
            case 4:
                return "Contributore Super";
        }
        return "Error";
    }

    public void incrementLevel() {
        if (this.level < 4) this.level++;
    }

    public static boolean isPasswordValid(String password) {
        if(password.matches(passwordRegex))
            return true;
        return false;
    }

    public boolean isPasswordValid() {
        if (this.password.matches(passwordRegex))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "User: " + this.username;
    }
}
