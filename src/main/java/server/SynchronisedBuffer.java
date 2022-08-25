package server;

import java.util.ArrayList;
import java.util.List;

public class SynchronisedBuffer {
    private final List<TriggerPoint> buffer = new ArrayList<>();
    private final Controller controller;

    public SynchronisedBuffer(Controller controller) {
        this.controller = controller;
    }
    public synchronized void put(TriggerPoint triggerPoint) {
        boolean contained = buffer.contains(triggerPoint);
        if (contained) {
            buffer.add(buffer.size(), triggerPoint);
        }
    }

    public synchronized void putAll(List<TriggerPoint> triggerPoints) {
        buffer.addAll(triggerPoints);
        notifyAll();
    }

    public synchronized TriggerPoint take() throws InterruptedException {
        while (buffer.isEmpty() || wholeListIsBlocked()) {
            wait();
        }
        TriggerPoint triggerPoint = buffer.get(0);
        buffer.remove(0);
        return triggerPoint;
    }

    private synchronized boolean wholeListIsBlocked() {
        if (buffer.isEmpty()) {
            return false;
        }
        for (TriggerPoint trigger : buffer) {
            if (!controller.getTrafficLightFromTrigger(trigger).isBlocked()) {
                return false;
            }
        }
        notifyAll();
        return true;
    }
}
