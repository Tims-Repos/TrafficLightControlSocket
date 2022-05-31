import java.net.Socket;

/**
 * Created by nathan on 26/03/2017.
 */
public class ClientInfo {
    public int userID=-1;
    public Socket mSocket = null;
    public ClientListener mClientListener = null;
    public ClientSender mClientSender = null;
}
