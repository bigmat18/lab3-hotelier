package ClientApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import Framework.Notify.DataTooLongException;
import Framework.Notify.NotifyReciever;

public class RankingNotify implements Runnable {
    private String NOTIFY_HOST;
    private int NOTIFY_PORT;
    private int NOTIFY_TIMEOUT;
    private NotifyReciever socket;
    private boolean isInGroup = false;

    RankingNotify(String host, int port, int timeout) throws IOException, SecurityException, IllegalArgumentException{
        this.NOTIFY_HOST = host;
        this.NOTIFY_PORT = port;
        this.NOTIFY_TIMEOUT = timeout;
        this.socket = new NotifyReciever(NOTIFY_PORT, InetAddress.getByName(NOTIFY_HOST), NOTIFY_TIMEOUT);
        this.socket.leaveGroup();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                byte[] received = this.socket.receiveNotify();
                if (received != null)
                    System.out.print("\033[0K\r[NEWS] - " + (new String(received)) + "\n> ");

            } catch (SocketTimeoutException e) {
                continue;
            } catch (DataTooLongException e) {
                System.out.println("[ERROR] Data received are too long.");
            } catch (Exception e) {
                System.out.println("[ERROR] Error in notify reciever");
            }
        }
    }

    public void joinGroup() throws IOException {
        if(this.isInGroup)
            return;

        this.socket.joinGroup();
        this.isInGroup = true;
    }

    public void leaveGroup() throws IOException {
        if(!this.isInGroup)
            return;

        this.socket.leaveGroup();
        this.isInGroup = false;
    }
}
