/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPoint {

    private String id;
    private String type;
    private int roadStatus;

    public TriggerPoint() {}

    public TriggerPoint(String id) {
        this.id = id;
    }

    public TriggerPoint(String id, String type, int roadStatus) {
        this.id = id;
        this.type = type;
        this.roadStatus = roadStatus;
    }

    public String getId() {
        return id;
    }

    public int getRoadStatus(){
        return roadStatus;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setRoadStatus(int roadStatus){
        this.roadStatus = roadStatus;
    }



    @Override
    public String toString(){
        return String.format("%s %s %d", id, type, roadStatus);

    }


}
