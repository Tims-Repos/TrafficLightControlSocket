package server;

import com.google.gson.annotations.Expose;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPoint {
    @Expose(serialize = false)
    private final int CAR_GREEN_TIME = 7000;
    @Expose(serialize = false)
    private final int BUS_GREEN_TIME = 10_000;
    @Expose(serialize = false)
    private final int PEDESTRIAN_GREEN_TIME = 120_000;
    private String id;
    private String type;
    private int status;

    public TriggerPoint() {}

    public TriggerPoint(String id) {
        this.id = id;
    }

    public TriggerPoint(String id, String type, int status) {
        this.id = id;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public int getStatus(){
        return status;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public boolean isTriggered() {
        return status == 1;
    }

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

    @Override
    public String toString(){
        return String.format("%s %s %d", id, type, status);

    }

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
