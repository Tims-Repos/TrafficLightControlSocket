package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Thread {
    private final int MAX_THREADS = 4;

    private ClientListener clientListener;
    private ClientSender clientSender;
    private ServerSocket serverSocket;
    private final TrafficLights trafficLights = new TrafficLights();
    private SynchronisedBuffer triggerPointList;
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
        triggerPointList = new SynchronisedBuffer(this);
    }

    public synchronized void deserializeMessage(String message) {
        try {
            gson = new Gson();
            TriggerPoints triggerPoints = gson.fromJson(message, TriggerPoints.class);
            if (triggerPoints.getTriggerpoints().size() > 0) {
                triggerPointList.putAll(triggerPoints.getTriggerpoints());
                //handleTriggers(triggerPoints);
            }
        } catch (JsonParseException exception) {
            System.out.println("This is no Json object >> " + message);
            throw new RuntimeException(exception);
        }
    }

    private synchronized Runnable getRunnable(TriggerPoint triggerPoint, TrafficLight trafficLight) {
        return new TrafficLightUpdater(this, trafficLight, triggerPoint);
    }

    private synchronized String serializeMessage() {
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(trafficLights);
    }

    public synchronized void sendMessageToClient() {
        trafficLights.updateTrafficLights();
        clientSender.sendMessage(serializeMessage());
    }


    public synchronized TrafficLight getTrafficLightFromTrigger(TriggerPoint triggerPoint) {
        return trafficLights.searchTrafficLightById(triggerPoint.getId());
    }

    @Override
    public void run() {
        ExecutorService threadpool = Executors.newFixedThreadPool(MAX_THREADS);

        try {
            while (!isInterrupted()) {
                TriggerPoint triggerPoint = triggerPointList.take();
                TrafficLight trafficLight = getTrafficLightFromTrigger(triggerPoint);
                if (triggerPoint.isTriggered()) {
                    if (!trafficLight.isBlocked()) {
                        trafficLight.setBlockListBlocked();
                        threadpool.execute(getRunnable(triggerPoint, trafficLight));
                    } else {
                        triggerPointList.put(triggerPoint);
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
}
