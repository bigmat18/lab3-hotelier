package Client;

public class AppStatus {
    private boolean running = true;
    private boolean logged = false;
    private String username = "";

    public boolean isRunning() { return this.running; }

    public boolean isLogged() { return this.logged; }

    public String getUsername() { return this.username; }

    public void setRunning(boolean running) { this.running = running; } 

    public void setLogged(boolean logged) { this.logged = logged; }

    public void setUsername(String username) { this.username = username; }
}
