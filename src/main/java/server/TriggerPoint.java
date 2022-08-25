package server;

import com.google.gson.annotations.Expose;

/**
 * Created by Timothy on 4-4-2017.
 */
public class TriggerPoint {
    @Expose(serialize = false)
    private final int CAR_GREEN_TIME = 7000;
    @Expose(serialize = false)
    private final int BUS_GREEN_TIME = 10_000;
    @Expose(serialize = false)
    private final int PEDESTRIAN_GREEN_TIME = 120_000;
    /**
     * The id, type and status are received as a JSON string from the client
     * and deserialized to these variables.
     */
    private String id;
    private String type;
    private int status;

    /**
     * @return the id of the triggerpoint.
     */
    public String getId() {
        return id;
    }

    /**
     * @return if the triggerpoint is triggered or not.
     */
    public boolean isTriggered() {
        return status == 1;
    }

    /**
     * @return the green time based on the type of vehicle.
     */
    public synchronized int getGreenTimeFromType() {
        int greenTime = CAR_GREEN_TIME;
        switch (type) {
            case "B":
                greenTime = BUS_GREEN_TIME;
                break;
            case "F":
            case "V":
                greenTime = PEDESTRIAN_GREEN_TIME;
                break;
        }
        return greenTime;
    }

    /**
     * @return the string representation of the triggerpoint object.
     */
    @Override
    public String toString(){
        return String.format("%s %s %d", id, type, status);

    }

    /**
     *
     * @param object the object to compare with this object.
     * @return if the passed objects id is equal to this objects id.
     */
    @Override
    public boolean equals(Object object)
    {
        boolean isEqual;
        if(!(object instanceof TriggerPoint)) {
            isEqual = false;
        } else {
            TriggerPoint triggerPoint = (TriggerPoint)object;
            isEqual = this.id.equals(triggerPoint.getId());
        }
        return isEqual;
    }


}
