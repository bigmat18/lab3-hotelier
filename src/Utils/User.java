package Utils;

public class User{
    private static final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private String password;
    private String username;

    public User(String password, String username) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() { return this.password; }

    public String getUsername() { return this.username; }

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

    public String toString() {
        return "User: " + this.username;
    }
}
