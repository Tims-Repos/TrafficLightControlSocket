import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by nathan on 26/03/2017.
 */
public class ClientSender extends Thread
{
    private final ArrayList<String> mMessageQueue = new ArrayList<String>();

    private final ServerDispatcher mServerDispatcher;
    private final ClientInfo mClientInfo;
    private final PrintWriter mOut;

    public ClientSender(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
        throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Adds given message to the message queue and notifies this thread
     * (actually getNextMessageFromQueue method) that a message is arrived.
     * sendMessage is called by other threads (ServerDispatcher).
     */
    public synchronized void sendMessage(String aMessage)
    {
        mMessageQueue.add(aMessage);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If the queue
     * is empty, falls in sleep until notified for message arrival by sendMessage
     * method.
     */
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        if (mMessageQueue.size()==0)
            wait();
        String message = mMessageQueue.get(0);
        mMessageQueue.remove(0);
        return message;
    }

    /**
     * Sends given message to the client's socket.
     */
    public void sendMessageToClient(String aMessage)
    {
        try {
            mOut.println(aMessage);
            mOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    @Override
    public void run()
    {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
        } catch (Exception e) {
            // Communication problem
            e.printStackTrace();
        }

        //Communication is broken. Interrupt both listener and sender threads
        mClientInfo.mClientListener.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
        System.out.println("Client disconnected");
    }
}
