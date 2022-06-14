import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Thread {
    private ClientListener clientListener;
    private ClientSender clientSender;
    private ServerSocket serverSocket;
    private final TrafficLights trafficLights = new TrafficLights();
    private final List<TriggerPoint> triggerPointList = new ArrayList<>();
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

    private Runnable setTrafficLightsStatus(TriggerPoint triggerPoint) {
        if (triggerPoint.getStatus() == 1) {
            TrafficLight trafficLight = trafficLights.searchTrafficLightById(triggerPoint.getId());
            if (!trafficLight.isBlocked()){
                trafficLight.setBlockListBlocked();
                return new TrafficLightUpdater(trafficLight, triggerPoint);
            } else {
                triggerPointList.add(triggerPoint);
            }
        } else {
            trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(0);
        }
        trafficLights.updateTrafficLights();
        return null;
    }

    private synchronized String serializeMessage() {
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(trafficLights);
    }

    public synchronized void sendMessageToClient(String message) {
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
        ExecutorService threadpool = Executors.newFixedThreadPool(8);

        try {
            while (!isInterrupted()) {
                TriggerPoint triggerPoint = getNextTriggerPointFromList();
                Runnable runnable = setTrafficLightsStatus(triggerPoint);
                if (runnable != null) {
                    threadpool.execute(runnable);
                }
            }
        } catch (InterruptedException e) {
            // Communication problem
            threadpool.shutdown();
            while(!threadpool.isTerminated()) {}
            System.out.println("Interrupted");
            throw new RuntimeException();
        }
    }

    public class TrafficLightUpdater implements Runnable {
        TrafficLight trafficLight;
        TriggerPoint triggerPoint;

        TrafficLightUpdater(TrafficLight trafficLight, TriggerPoint triggerPoint) {
            this.trafficLight = trafficLight;
            this.triggerPoint = triggerPoint;
        }

        @Override
        public void run() {
            int greenTime = getGreenDelayFromType(triggerPoint);
            System.out.println("Triggered: " + trafficLight.toString());
            trafficLight.setBlockListStatus(1);
            trafficLights.updateTrafficLights();
            sendMessageToClient(serializeMessage());
            System.out.println(trafficLight.getId() + ": Orange: " + trafficLight.blockList.toString());
            System.out.println(trafficLight.getId() + ": Waiting 2 seconds");
            delay(2000);
            trafficLight.setBlockListStatus(0);
            trafficLight.setStatus(2);
            trafficLights.updateTrafficLights();
            sendMessageToClient(serializeMessage());
            System.out.println(trafficLight.getId() + ": Red: " + trafficLight.blockList.toString());
            System.out.println(trafficLight.getId() + ": Green: " + trafficLight.toString());
            System.out.printf(trafficLight.getId() + ": Waiting " + greenTime/1000 + " seconds\n\n");
            delay(greenTime);
            trafficLight.setBlockListUnblocked();
        }
    }

    public int getGreenDelayFromType(TriggerPoint triggerPoint) {
        int greenTime = 7000;
        switch (triggerPoint.getType()) {
            case "B":
                greenTime = 10000;
                break;
            case "F":
            case "V":
                greenTime = 8000;
                break;
        }
        return greenTime;
    }
}
