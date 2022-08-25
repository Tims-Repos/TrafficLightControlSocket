import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Vector;

//import org.omg.Messaging.SYNC_WITH_TRANSPORT;
//import sun.applet.Main;

/**
 * Created by nathan on 26/03/2017.
 */
    public class ServerDispatcher {
    private final Vector<ClientInfo> mClients = new Vector<ClientInfo>();
    private TrafficLights trafficLights;
    private Gson gson;


    public ServerDispatcher() {
        Initialize();
    }

    private void Initialize() {
        trafficLights = new TrafficLights();
        trafficLights.initializeTrafficLights();
    }

    public synchronized void addClient(ClientInfo aClientInfo) {
        mClients.add(aClientInfo);
    }

    public synchronized void deleteClient(ClientInfo aClientInfo) {
        int clientIndex = mClients.indexOf(aClientInfo);
        if (clientIndex != -1)
            mClients.removeElementAt(clientIndex);
    }

    public synchronized void sendMessageToAllClients(TriggerPoints triggerPoints) {

        for (TriggerPoint triggerPoint : triggerPoints.getTriggerpoints()) {
            if (triggerPoint.getStatus() == 1) {
                trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(2);
            } else {
                trafficLights.searchTrafficLightById(triggerPoint.getId()).setStatus(0);
            }
        }

        trafficLights.updateTrafficLights();
        gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final String aMessage = gson.toJson(trafficLights);
        System.out.println(aMessage);
        for (ClientInfo info : mClients) {
            info.mClientSender.sendMessage(aMessage);
        }
        delay(2000);
    }

    public void sendMessage(ClientInfo aClientInfo, String aMessage) {
        aClientInfo.mClientSender.sendMessage(aMessage);
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
