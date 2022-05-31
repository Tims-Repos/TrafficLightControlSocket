import java.util.ArrayList;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TrafficLights {
    private ArrayList<TrafficLight> trafficlights = new ArrayList<TrafficLight>();

    private String[] trafficIds = {"XOZ", "XOW","XZO", "XZW", "XWO", "XWZ",
            "YNO", "YNW", "YON", "YOW", "YWN", "YWO",
            "BNO", "BNW", "BZO",
            "FN", "FO", "FZ", "FW",
            "VN", "VNM", "VO", "VOM", "VZ", "VZM", "VW", "VWM"};


    public void initializeTrafficLights() {
        for (String trafficId : trafficIds) {
            trafficlights.add(new TrafficLight(trafficId));
        }
    }

    public TrafficLights() {}

    public TrafficLights(ArrayList<TrafficLight> trafficLights) {
        this.trafficlights = trafficLights;
    }

    public ArrayList<TrafficLight> getTrafficlights() {
        return trafficlights;
    }

    public void setTrafficlights(final ArrayList<TrafficLight> trafficlights) {
        this.trafficlights = trafficlights;
    }

    public void addTrafficLight(TrafficLight trafficLight) {
        trafficlights.add(trafficLight);
    }

    public TrafficLight searchTrafficLightById(String id) {
        for (TrafficLight trafficLight : trafficlights) {
            if (trafficLight.getId().equals(id)) {
                return trafficLight;
            }
        }
        return null;
    }


    @Override
    public String toString(){
        final StringBuilder formatted = new StringBuilder();
        for (final TrafficLight trafficLight : trafficlights) {
            formatted.append("\n >> ").append(trafficLight);

        }

        return formatted.toString();
    }

}
