package server;

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
    private final int CAR_GREEN_TIME = 7000;
    private final int BUS_GREEN_TIME = 10_000;
    private final int PEDESTRIAN_GREEN_TIME = 120_000;
    private final int ORANGE_TIME = 2000;
    private final int MAX_THREADS = 4;

    private ClientListener clientListener;
    private ClientSender clientSender;
    private ServerSocket serverSocket;
    private final TrafficLights trafficLights = new TrafficLights();
    private final List<TriggerPoint> triggerPointList = new ArrayList<>();
    private Gson gson;

    public Controller() {
        initialize();
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

    private void initialize() {
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
            System.out.println("This is no Json object >> " + message);
            throw new RuntimeException(exception);
        }
    }

    private synchronized Runnable getRunnable(TriggerPoint triggerPoint, TrafficLight trafficLight) {
        return new TrafficLightUpdater(trafficLight, triggerPoint);
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
        notifyAll();
    }

    private synchronized TriggerPoint getNextTriggerPointFromList() throws InterruptedException
    {
        while (triggerPointList.isEmpty() || wholeListIsBlocked()) {
            wait();
        }
        System.out.println(triggerPointList.toString());
        TriggerPoint triggerPoint = triggerPointList.get(0);
        triggerPointList.remove(0);
        return triggerPoint;
    }

    public synchronized void addToListIfNotPresent(TriggerPoint triggerPoint) {
        boolean contained = triggerPointList.contains(triggerPoint);
        if (contained) {
            triggerPointList.add(triggerPointList.size(), triggerPoint);
            System.out.println(triggerPointList.toString());
        }
    }

    private synchronized TrafficLight getTrafficLightFromTrigger(TriggerPoint triggerPoint) {
        return trafficLights.searchTrafficLightById(triggerPoint.getId());
    }

    private synchronized boolean wholeListIsBlocked() {
        if (triggerPointList.isEmpty()) {
            return false;
        }
        for (TriggerPoint trigger : triggerPointList) {
            if (!trafficLights.searchTrafficLightById(trigger.getId()).isBlocked()) {
                return false;
            }
        }
        notify();
        return true;
    }

    @Override
    public void run() {
        ExecutorService threadpool = Executors.newFixedThreadPool(MAX_THREADS);

        try {
            while (!isInterrupted()) {
                TriggerPoint triggerPoint = getNextTriggerPointFromList();
                TrafficLight trafficLight = getTrafficLightFromTrigger(triggerPoint);
                if (triggerPoint.isTriggered()) {
                    if (!trafficLight.isBlocked()) {
                        trafficLight.setBlockListBlocked();
                        threadpool.execute(getRunnable(triggerPoint, trafficLight));
                    } else {
                        addToListIfNotPresent(triggerPoint);
                    }
                } else {
                    trafficLight.setRed();
                    trafficLights.updateTrafficLights();
                }
            }
        } catch (InterruptedException e) {
            // Communication problem
            threadpool.shutdown();
            while(!threadpool.isTerminated()) {}
            System.out.println("Interrupted");
            throw new RuntimeException(e);
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
            int greenTime = getGreenTimeFromType(triggerPoint);
            System.out.println("Triggered: " + trafficLight.toString());
            trafficLight.setBlockListOrange();
            trafficLights.updateTrafficLights();
            sendMessageToClient(serializeMessage());
            delay(ORANGE_TIME);
            trafficLight.setBlockListRed();
            trafficLight.setGreen();
            trafficLights.updateTrafficLights();
            sendMessageToClient(serializeMessage());
            delay(greenTime);
            trafficLight.setBlockListUnblocked();
        }
    }

    public synchronized int getGreenTimeFromType(TriggerPoint triggerPoint) {
        int greenTime = CAR_GREEN_TIME;
        switch (triggerPoint.getType()) {
            case "B":
                greenTime = BUS_GREEN_TIME;
                break;
            case "F":
            case "V":
                greenTime = PEDESTRIAN_GREEN_TIME;
                break;
        }
        return greenTime;
    }

    private void delay(int delay){
        try {
            sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
