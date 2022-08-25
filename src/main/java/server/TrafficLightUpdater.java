package server;

import static java.lang.Thread.sleep;

/**
 * Created by Timothy on 26/06/2022.
 */
public class TrafficLightUpdater implements Runnable {
    private final int ORANGE_TIME = 2000;

    private TrafficLight trafficLight;
    private TriggerPoint triggerPoint;
    private Controller controller;

    /**
     * The TrafficLightUpdater constructor which sets the parameters to the
     * corresponding fields.
     * @param controller The controller that it uses to call the sendMessage
     *                   method.
     * @param trafficLight The traffic light to update
     * @param triggerPoint the trigger point from which we get the green time
     *                     based on it's type.
     */
    TrafficLightUpdater(Controller controller, TrafficLight trafficLight, TriggerPoint triggerPoint) {
        this.controller = controller;
        this.trafficLight = trafficLight;
        this.triggerPoint = triggerPoint;
    }

    /**
     * The task to run. Setting the block list of the current traffic light
     * to orange for certain amount of seconds, then set it's block list to
     * red and the current traffic light to green for certain amount of
     * seconds.
     */
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

    /**
     *
     * @param delay The time to delay the executing thread.
     */
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
