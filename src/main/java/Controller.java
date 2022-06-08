import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller extends Thread {
    private ClientListener clientListener;
    private ClientSender clientSender;
    private ServerSocket serverSocket;
    private TrafficLights trafficLights;
    private Gson gson;

    public Controller() {
        Initialize();
    }

    public void initializeSocketConnection(int portNumber) {
        try
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started on port " + portNumber);
        }
        catch (IOException se)
        {
            System.err.println("Can not start listening on port " + portNumber);
            se.printStackTrace();
            System.exit(-1);
        }
    }

    public void startCommunicationThreads() {
        while (true) {
            try
            {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");
                clientListener =
                        new ClientListener(socket, this);
                clientSender =
                        new ClientSender(socket);
                clientListener.start();
                clientSender.start();
            }
            catch (Exception e) {
                e.printStackTrace();
                clientSender.interrupt();
                clientListener.interrupt();
                System.out.println("Client disconnected");
            }
        }
    }

    private void Initialize() {
        trafficLights = new TrafficLights();
        trafficLights.initializeTrafficLights();
    }

    public synchronized void deserializeMessage(String message) {
        try {
            gson = new Gson();
            TriggerPoints triggerPoints = gson.fromJson(message, TriggerPoints.class);
            if (triggerPoints.getTriggerpoints().size() > 0) {
                handleTriggers(triggerPoints);
            }
        } catch (JsonParseException exception) {
            System.out.println(("This is no Json object >> " + message));
            throw new RuntimeException();
        }
    }

    private synchronized void handleTriggers(TriggerPoints triggerPoints) {
        for (TriggerPoint triggerPoint : triggerPoints.getTriggerpoints()) {
            setTrafficLightsStatus(triggerPoint);
        }
        trafficLights.updateTrafficLights();
        sendMessageToClient(serializeMessage());
    }

    private synchronized void setTrafficLightsStatus(TriggerPoint triggerPoint) {
        if (triggerPoint.getStatus() == 1) {
            trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(2);
        } else {
            trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(0);
        }
    }

    private synchronized String serializeMessage() {
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(trafficLights);
    }

    public synchronized void sendMessageToClient(String message) {
        System.out.println(message);
        clientSender.sendMessage(message);
        delay(2000);
    }

    public void delay(int delay){
        Thread t = new Thread();
        t.start();
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
