import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by nathan on 26/03/2017.
 */
public class ClientListener extends Thread {
    private final Controller controller;
    private final Socket socket;

    public ClientListener(Socket socket, Controller controller) {
        this.controller = controller;
        this.socket = socket;
    }

    /**
     * Until interrupted, reads messages from the client socket, forwards them
     * to the server dispatcher and notifies the server dispatcher.
     */
    @Override
    public void run() {
        String message;

        while (!isInterrupted()) {
            try {
                InputStreamReader input = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(input);
                message = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            System.out.println(message);
            if (message == null)
                break;

            controller.deserializeMessage(message);

        }
        throw new RuntimeException("From clientListener: ");
    }
}
