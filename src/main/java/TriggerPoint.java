/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPoint {

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



    @Override
    public String toString(){
        return String.format("%s %s %d", id, type, status);

    }


}
