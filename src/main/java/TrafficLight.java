import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gebruiker on 4-4-2017.
 */
class TrafficLight {
    @Expose
    private String id;
    @Expose
    private int status;
    @Expose(serialize = false)
    List<TrafficLight> blockList = new ArrayList<>();
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

    public void setStatus(int status) {
        this.status = status;
    }

    public synchronized void setBlockListBlocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setBlocked(true);
        }
    }

    public synchronized void setBlockListUnblocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setBlocked(false);
        }
    }

    public synchronized void setBlockListStatus(int status) {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setStatus(status);
        }
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

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }


    @Override
    public String toString() {
        return String.format("%s %d", id, status);

    }


}
