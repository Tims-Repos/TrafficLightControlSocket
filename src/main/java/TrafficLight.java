import com.google.gson.annotations.Expose;

/**
 * Created by gebruiker on 4-4-2017.
 */
class TrafficLight {
    @Expose
    private String id;
    @Expose
    private int status;

    public TrafficLight() {

    }

    public TrafficLight(String id) {
        this.id = id;
    }

    public TrafficLight(String id, int colorStatus) {
        this.id = id;
        this.status = colorStatus;
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


    @Override
    public String toString() {
        return String.format("%s %d", id, status);

    }


}
