package server;

/**
 * Created by nathan on 26/03/2017.
 */
public class Server {
    // The port on which we connect to the client.
    public static final int LISTENING_PORT = 1337;

    public static void main(String[] args)
    {
        Controller controller = new Controller();
        controller.start();
        controller.initializeSocketConnection(LISTENING_PORT);
        controller.startCommunicationThreads();
    }
}
