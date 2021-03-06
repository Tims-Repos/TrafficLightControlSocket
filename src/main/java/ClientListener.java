import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by nathan on 26/03/2017.
 */
public class ClientListener extends Thread {
    private final ServerDispatcher mServerDispatcher;
    private final ClientInfo mClientInfo;
    private final Socket socket;
    private BufferedReader mIn;
    private String message;
    private final Gson gson = new Gson();


    public ClientListener(ClientInfo aClientInfo,
                          ServerDispatcher aServerDispatcher) {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        socket = aClientInfo.mSocket;

    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher and notifies the server dispatcher.
     */
    @Override
    public void run() {
        message = "";

        while (!isInterrupted()) {
            try {
                InputStreamReader input = new InputStreamReader(socket.getInputStream());
                mIn = new BufferedReader(input);
                message = mIn.readLine();
                System.out.println(message);
                if (message == null)
                    break;
                TriggerPoints triggerPoints = gson.fromJson(message, TriggerPoints.class);
                try {
                    if (triggerPoints.getTriggerpoints().size() > 0) {
                        mServerDispatcher.sendMessageToAllClients(triggerPoints);
                    }
                    //System.out.println(triggerPoints);
                } catch (Exception e) {
                    System.out.println(("This is no Json object >> " + message));
                    mClientInfo.mClientSender.interrupt();
                    mServerDispatcher.deleteClient(mClientInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
}
