import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TrafficLights {
    @Expose
    private ArrayList<TrafficLight> trafficlights;
    @Expose(serialize = false)
    private final Map<String, TrafficLight> trafficLightMap = new LinkedHashMap<String, TrafficLight>();
    @Expose(serialize = false)
    private final String[] trafficIds = {"XOZ", "XOW","XZO", "XZW", "XWO", "XWZ",
            "YNO", "YNW", "YON", "YOW", "YWN", "YWO",
            "BNW", "BZO",
            "FN", "FO", "FZ", "FW",
            "VN", "VNM", "VO", "VOM", "VZ", "VZM", "VW", "VWM"};


    public void initializeTrafficLights() {
        for (String trafficId : trafficIds) {
            TrafficLight temp = new TrafficLight(trafficId);
            trafficLightMap.put(trafficId, temp);
        }
        trafficlights = new ArrayList<TrafficLight>(trafficLightMap.values());
    }

    public TrafficLights() {}

    public TrafficLight searchTrafficLightById(String id) {
        return trafficLightMap.get(id);
    }

    public ArrayList<TrafficLight> getTrafficlights() {
        trafficlights = new ArrayList<TrafficLight>(trafficLightMap.values());
        return trafficlights;
    }

    public void updateTrafficLights() {
        trafficlights = new ArrayList<TrafficLight>(trafficLightMap.values());
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
