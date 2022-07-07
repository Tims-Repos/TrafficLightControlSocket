package server;

import static java.lang.Thread.sleep;

public class TrafficLightUpdater implements Runnable {
    private final int ORANGE_TIME = 2000;

    private TrafficLight trafficLight;
    private TriggerPoint triggerPoint;
    private Controller controller;

    TrafficLightUpdater(Controller controller, TrafficLight trafficLight, TriggerPoint triggerPoint) {
        this.controller = controller;
        this.trafficLight = trafficLight;
        this.triggerPoint = triggerPoint;
    }

    @Override
    public void run() {
        int greenTime = triggerPoint.getGreenTimeFromType();
        System.out.println("Triggered: " + trafficLight.toString());
        trafficLight.setBlockListOrange();
        controller.sendMessageToClient();
        delay(ORANGE_TIME);
        trafficLight.setBlockListRed();
        trafficLight.setGreen();
        controller.sendMessageToClient();
        delay(greenTime);
        trafficLight.setBlockListUnblocked();
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
