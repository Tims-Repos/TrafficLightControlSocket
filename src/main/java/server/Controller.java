package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Timothy on 04/04/2017.
 */
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

    /**
     * Initializes the socket connection with the client using the port number given.
     * @param portNumber Used to initialize the socket connection on that port.
     */
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

    /**
     * Start the client listener and client sender threads to communicate with
     * the client.
     */
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

    /**
     * initialize is used to call the initializeTrafficLights method of
     * the trafficLights object and to create a new SynchronisedBuffer
     * object which is used for storing the received trigger points.
     */
    private void initialize() {
        trafficLights.initializeTrafficLights();
        triggerPointList = new SynchronisedBuffer(this);
    }

    /**
     * deserializeMessage is used to create objects from the received JSON
     * string. It uses Googles gson library to do the conversion between
     * JSON and Java objects. It than adds all the trigger point objects
     * to the triggerPointList.
     * @param message The JSON string received from the client.
     */
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

    /**
     * This method is used to create a runnable called TrafficLightUpdater.
     * Essentially it is a task that takes care of updating traffic lights
     * based on the received triggers.
     * @param triggerPoint The triggerPoint that is triggered on the client side.
     * @param trafficLight The traffic light that corresponds with the
     *                     triggerPoint.
     * @return The runnable task to update the traffic lights that can't be
     * green when this one is green.
     */
    private synchronized Runnable getRunnable(TriggerPoint triggerPoint, TrafficLight trafficLight) {
        return new TrafficLightUpdater(this, trafficLight, triggerPoint);
    }

    /**
     * This method is used to serialize a Java Object to a JSON string using
     * Google's GSON library. It converts all the traffic Lights that changed
     * to a string.
     * @return a JSON string.
     */
    private synchronized String serializeMessage() {
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(trafficLights);
    }

    /**
     * This method is used to send a message to the client. Under water it
     * calls the clientSenders object sendmessage which needs the JSON
     * string to send. Therefore it gets the serializeMessage method as an
     * argument.
     */
    public synchronized void sendMessageToClient() {
        trafficLights.updateTrafficLights();
        clientSender.sendMessage(serializeMessage());
    }

    /**
     * This method calls the searchTrafficLightById method from the
     * TrafficLights object. It needs a triggerpoint to get the
     * corresponding traffic light
     * @param triggerPoint the triggerPoint which TrafficLight object we need.
     * @return The TrafficLight object returned from the search.
     */
    public synchronized TrafficLight getTrafficLightFromTrigger(TriggerPoint triggerPoint) {
        return trafficLights.searchTrafficLightById(triggerPoint.getId());
    }

    /**
     * Here we create a threadpool which runs all the runnable tasks that we
     * get from the getRunnable object. It uses the first triggerpoint from
     * the triggerPointList and its corresponding trafficLight to call
     * the getRunnable method and get the task to run.
     */
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