package server;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy on 4-4-2017.
 */
class TrafficLight {
    /**
     * Constant values for the corresponding traffic lights.
     */
    @Expose(serialize = false)
    private final int RED = 0;
    @Expose(serialize = false)
    private final int ORANGE = 1;
    @Expose(serialize = false)
    private final int GREEN = 2;

    //The traffic lights that are blocked when this traffic light is green.
    @Expose(serialize = false)
    private List<TrafficLight> blockList = new ArrayList<>();
    //Variable to check if the traffic light is blocked or not.
    @Expose(serialize = false)
    private boolean isBlocked = false;

    /**
     * The id and status variables are serialized and send to the client.
     */
    @Expose
    private String id;
    @Expose
    private int status;

    /**
     * The contstructor of the traffic light, made by providing an id as argument.
     * @param id the id of the traffic light.
     */
    public TrafficLight(String id) {
        this.id = id;
    }

    /**
     * Gets the id of the traffic light
     * @return the String id of the traffic light.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the current status of the traffic light.
     * @return the current int status. (0,1,2)
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the new status of the traffic light.
     * @param status The new int status of the traffic light.
     */
    private synchronized void setStatus(int status) {
        this.status = status;
    }

    /**
     * Calls setStatus to set the traffic lights status to 0 (red).
     */
    public synchronized void setRed() {
        setStatus(RED);
    }

    /**
     * Calls setStatus to set the traffic lights status to 1 (orange).
     */
    public synchronized void setOrange() {
        setStatus(ORANGE);
    }

    /**
     * Calls setStatus to set the traffic lights status to 2 (green).
     */
    public synchronized void setGreen() {
        setStatus(GREEN);
    }

    /**
     * Sets the currents traffic lights block list to blocked.
     */
    public synchronized void setBlockListBlocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setBlocked();
        }
    }

    /**
     * Sets the current traffic lights block list to unblocked.
     */
    public synchronized void setBlockListUnblocked() {
        for (TrafficLight trafficLight : blockList) {
            trafficLight.setUnblocked();
        }
    }

    /**
     * Sets the block list status based on each traffic lights own current
     * status.
     * @param status the status to set the traffic light to.
     */
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

    /**
     * Sets the status of the whole block list to red.
     */
    public synchronized void setBlockListRed() {
        setBlockListStatus(RED);
    }

    /**
     * Sets the status of the whole block list to orange.
     */
    public synchronized void setBlockListOrange() {
        setBlockListStatus(ORANGE);
    }

    /**
     * Sets the block list of the current traffic light.
     * @param blockList The list that is the block list of
     *                  the current traffic light.
     */
    public void setBlockList(List<TrafficLight> blockList) {
        this.blockList = blockList;
    }

    /**
     * Check if the current traffic light is blocked.
     * @return the isBlocked variable value.
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Set the current traffic lights isBlocked value to true.
     */
    public void setBlocked() {
        isBlocked = true;
    }

    /**
     * Set the current traffic lights isBlocked value to false.
     */
    public void setUnblocked() {
        isBlocked = false;
    }

    /**
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        return String.format("%s %d", id, status);

    }


}
