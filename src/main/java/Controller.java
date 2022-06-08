import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread {
    private ClientListener clientListener;
    private ClientSender clientSender;
    private ServerSocket serverSocket;
    private TrafficLights trafficLights;
    private List<TriggerPoint> triggerPointList = new ArrayList<>();
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
                addToTriggerPointList(triggerPoints);
                //handleTriggers(triggerPoints);
            }
        } catch (JsonParseException exception) {
            System.out.println(("This is no Json object >> " + message));
            throw new RuntimeException();
        }
    }

    private synchronized void setTrafficLightsStatus(TriggerPoint triggerPoint) {
        if (triggerPoint.getStatus() == 1) {
            TrafficLight trafficLight = trafficLights.searchTrafficLightById(triggerPoint.getId());
            if (!trafficLight.isBlocked()){
                trafficLight.setBlockListBlocked();
                Thread t =
                        new Thread(() -> {
                            trafficLight.setBlockListStatus(1);
                            trafficLights.updateTrafficLights();
                            sendMessageToClient(serializeMessage());
                            delay(3000);
                            trafficLight.setBlockListStatus(0);
                            trafficLight.setStatus(2);
                            trafficLights.updateTrafficLights();
                            sendMessageToClient(serializeMessage());
                            delay(7000);
                            trafficLight.setBlockListUnblocked();
                            triggerPointList.remove(triggerPoint);

                        });
                t.start();
            }
        } else {
            trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(0);
        }
        trafficLights.updateTrafficLights();
    }

    private synchronized String serializeMessage() {
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(trafficLights);
    }

    public synchronized void sendMessageToClient(String message) {
        System.out.println(message);
        clientSender.sendMessage(message);
    }

    private synchronized void addToTriggerPointList(TriggerPoints triggerPoints) {
        triggerPointList.addAll(triggerPoints.getTriggerpoints());
        notify();
    }

    public synchronized void delay(int delay){
        try {
            sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private synchronized TriggerPoint getNextTriggerPointFromList() throws InterruptedException
    {
        while (triggerPointList.size()==0)
            wait();
        TriggerPoint triggerPoint = triggerPointList.get(0);
        triggerPointList.remove(0);
        return triggerPoint;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                TriggerPoint triggerPoint = getNextTriggerPointFromList();
                setTrafficLightsStatus(triggerPoint);
            }
        } catch (InterruptedException e) {
            // Communication problem
            System.out.println("Interrupted");
            throw new RuntimeException();
        }
    }
}
