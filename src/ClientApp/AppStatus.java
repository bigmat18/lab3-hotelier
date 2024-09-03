package ClientApp;

public class AppStatus {
    private boolean running = true;
    private String token = null;
    private RankingNotify notify;

    public boolean isRunning() { return this.running; }

    public boolean isLogged() { return this.token != null; }

    public String getToken() { return this.token; }

    public RankingNotify getNotify() { return this.notify; }

    public void setRunning(boolean running) { this.running = running; }

    public void setToken(String token) { this.token = token; }

    public void setNotify(RankingNotify notify) { this.notify = notify; }
}
