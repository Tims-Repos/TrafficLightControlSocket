package server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy on 26/06/2022
 */
public class SynchronisedBuffer {
    private final List<TriggerPoint> buffer = new ArrayList<>();
    private final Controller controller;

    /**
     * Constructor for our SynchronisedBuffer class. It sets the
     * controller field.
     * @param controller used to call the getTrafficLightsFromTrigger method.
     */
    public SynchronisedBuffer(Controller controller) {
        this.controller = controller;
    }

    /**
     * Puts the triggerPoint object inside of the list if it is not yet
     * contained.
     * @param triggerPoint the triggerPoint to put inside of the list.
     */
    public synchronized void put(TriggerPoint triggerPoint) {
        boolean contained = buffer.contains(triggerPoint);
        if (contained) {
            buffer.add(buffer.size(), triggerPoint);
        }
    }

    /**
     * Put a list of triggerpoint objects into the list.
     * Notify the take function that there's
     * a new item in the list.
     * @param triggerPoints the list of triggerpoint objects to be added
     *                      to the bufferedlist.
     */
    public synchronized void putAll(List<TriggerPoint> triggerPoints) {
        buffer.addAll(triggerPoints);
        notifyAll();
    }

    /**
     * This method takes the first triggerpoint from the list if it's not
     * empty or if there are triggerpoints whose trafficlights are still
     * unblocked. Otherwise it waits until evaluated false.
     * @return The triggerpoint that is taken from the bufferedlist.
     * @throws InterruptedException if set to wait state.
     */
    public synchronized TriggerPoint take() throws InterruptedException {
        while (wholeListIsBlocked()) {
            wait();
        }
        TriggerPoint triggerPoint = buffer.get(0);
        buffer.remove(0);
        return triggerPoint;
    }

    /**
     * Checks if the list if empty or the triggerpoints corresponding
     * traffic light is blocked.
     * @return false if unblocked and true if empty or blocked.
     */
    private synchronized boolean wholeListIsBlocked() {
        if (buffer.isEmpty()) {
            return true;
        }
        for (TriggerPoint trigger : buffer) {
            if (!controller.getTrafficLightFromTrigger(trigger).isBlocked()) {
                notifyAll();
                return false;
            }
        }
        return true;
    }
}
