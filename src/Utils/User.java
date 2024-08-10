package Utils;

public class User{

    private int id;
    private String password;
    private String username;

    public User(String password, String username) {
        this(-1, password, username);
    }

    public User(int id, String password, String username) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() { return this.id; }

    public String getPassword() { return this.password; }

    public String getUsername() { return this.username; }

    public boolean isPasswordValid() {
        if(this.password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"))
            return true;
        return false;
    }

    public String toString() {
        return "User: " + this.username;
    }
}
