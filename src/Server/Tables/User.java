package Server.Tables;

public class User{
    private int id;
    private String password;
    private String email;

    public User(int id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public int getId() { return this.id; }

    public String toString() {
        return this.email;
    }
}
