package server;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gebruiker on 4-4-2017.
 */
class TrafficLight {
    @Expose(serialize = false)
    private final int RED = 0;
    @Expose(serialize = false)
    private final int ORANGE = 1;
    @Expose(serialize = false)
    private final int GREEN = 2;

    @Expose
    private String id;
    @Expose
    private int status;
    @Expose(serialize = false)
    private List<TrafficLight> blockList = new ArrayList<>();
    @Expose(serialize = false)
    private boolean isBlocked = false;

    public TrafficLight() {

    }

    public TrafficLight(String id) {
        this.id = id;
    }

    public TrafficLight(String id, int status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    private synchronized void setStatus(int status) {
        this.status = status;
    }

    public synchronized void setRed() {
        setStatus(RED);
    }

    public synchronized void setOrange() {
        setStatus(ORANGE);
    }
    public synchronized void setGreen() {
        setStatus(GREEN);
    }

    public synchronized void setBlockListBlocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setBlocked();
        }
    }

    public synchronized void setBlockListUnblocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setUnblocked();
        }
    }

    private synchronized void setBlockListStatus(int status) {
        for (TrafficLight trafficLight : blockList) {
            switch (status) {
                case RED:
                    if (trafficLight.getStatus() != RED) {
                        trafficLight.setStatus(status);
                    }
                    break;
                case ORANGE:
                    if (trafficLight.getStatus() == GREEN) {
                        trafficLight.setStatus(status);
                    }
                    break;
            }
        }
    }

    public synchronized void setBlockListRed() {
        setBlockListStatus(RED);
    }

    public synchronized void setBlockListOrange() {
        setBlockListStatus(ORANGE);
    }

    public void setBlockList(List<TrafficLight> blockList) {
        this.blockList = blockList;
    }

    public synchronized List<TrafficLight> getBlockList() {
        return blockList;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked() {
        isBlocked = true;
    }

    public void setUnblocked() {
        isBlocked = false;
    }


    @Override
    public String toString() {
        return String.format("%s %d", id, status);

    }


}
