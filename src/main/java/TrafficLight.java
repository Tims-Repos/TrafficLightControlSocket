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
    List<String> blockList = new ArrayList<>();
    @Expose(serialize = false)
    boolean isBlocked = false;

    public TrafficLight() {

    }

    public TrafficLight(String id) {
        this.id = id;
    }

    public TrafficLight(String id, int colorStatus) {
        this.id = id;
        this.status = colorStatus;
    }

    public TrafficLight(String id, int colorStatus, List<String> blockList) {
        this.id = id;
        this.status = colorStatus;
        this.blockList = blockList;
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
