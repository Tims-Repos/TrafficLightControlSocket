import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by nathan on 26/03/2017.
 */
public class Server {

    public static final int LISTENING_PORT = 1337;

    public static void main(String[] args)
    {

        // Open server socket for listening
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(LISTENING_PORT);
            System.out.println("Server started on port " + LISTENING_PORT);
        }
        catch (IOException se)
        {
            System.err.println("Can not start listening on port " + LISTENING_PORT);
            se.printStackTrace();
            System.exit(-1);
        }

        // Start ServerDispatcher thread
        ServerDispatcher serverDispatcher = new ServerDispatcher();

        while (true) {
            try
            {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");
                ClientInfo clientInfo = new ClientInfo();
                clientInfo.mSocket = socket;
                ClientListener clientListener =
                        new ClientListener(clientInfo, serverDispatcher);
                ClientSender clientSender =
                        new ClientSender(clientInfo, serverDispatcher);
                clientInfo.mClientListener = clientListener;
                clientInfo.mClientSender = clientSender;
                clientListener.start();
                clientSender.start();
                serverDispatcher.addClient(clientInfo);
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
}
