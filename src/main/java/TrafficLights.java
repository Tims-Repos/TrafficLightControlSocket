import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TrafficLights {
    @Expose
    private ArrayList<TrafficLight> trafficlights;
    @Expose(serialize = false)
    private final Map<String, TrafficLight> trafficLightMap = new LinkedHashMap<>();

    public void initializeTrafficLights() {
        createTrafficLights();
        for (TrafficLight trafficLight : trafficlights) {
            trafficLightMap.put(trafficLight.getId(), trafficLight);
        }
        trafficlights = new ArrayList<>(trafficLightMap.values());
    }

    public TrafficLights() {}

    public TrafficLight searchTrafficLightById(String id) {
        return trafficLightMap.get(id);
    }

    public ArrayList<TrafficLight> getTrafficlights() {
        trafficlights = new ArrayList<>(trafficLightMap.values());
        return trafficlights;
    }

    public void updateTrafficLights() {
        trafficlights = new ArrayList<>(trafficLightMap.values());
    }

    private void createTrafficLights() {
        trafficlights.add(new TrafficLight(
                "XOZ", new ArrayList<>(Arrays.asList(
                "XZW", "VZ", "VZM", "XWO", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "XOW", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "XZW"))));
        trafficlights.add(new TrafficLight(
                "XZO", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "XWO"))));
        trafficlights.add(new TrafficLight(
                "XZW", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "FW", "VW", "VWM", "XOZ", "XOW", "XWO"))));
        trafficlights.add(new TrafficLight(
                "XWO", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "XZW", "XOZ", "XZO", "BZO"))));
        trafficlights.add(new TrafficLight(
                "XWZ", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "FZ", "VZ", "VZM", "XOZ"))));
        trafficlights.add(new TrafficLight(
                "YNO", new ArrayList<>(Arrays.asList(
                        "VN", "VNM", "FN", "FO", "VO", "VOM", "YWN", "YOW", "YWN", "YWO"))));
        trafficlights.add(new TrafficLight(
                "YNW", new ArrayList<>(Arrays.asList(
                        "VN", "VNM", "FN", "YOW"))));
        trafficlights.add(new TrafficLight(
                "YON", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "FN", "VN", "VNM", "YWN" ))));
        trafficlights.add(new TrafficLight(
                "YOW", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "YWN", "BNW", "YNO", "YNW"))));
        trafficlights.add(new TrafficLight(
                "YWN", new ArrayList<>(Arrays.asList(
                        "YNO", "YOW", "YON", "FN", "VN", "VNM"))));
        trafficlights.add(new TrafficLight(
                "YWO", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "YNO"))));
        trafficlights.add(new TrafficLight(
                "BNW", new ArrayList<>(Arrays.asList(
                        "FN", "VN", "VNM", "YNO", "YNW", "YOW"))));
        trafficlights.add(new TrafficLight(
                "BZO", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "XZO", "XWO"))));
        trafficlights.add(new TrafficLight(
                "FN", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN"))));
        trafficlights.add(new TrafficLight(
                "FO", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON"))));
        trafficlights.add(new TrafficLight(
                "FZ", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "FW", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "VN", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN"))));
        trafficlights.add(new TrafficLight(
                "VNM", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN"))));
        trafficlights.add(new TrafficLight(
                "VO", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON"))));
        trafficlights.add(new TrafficLight(
                "VOM", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON"))));
        trafficlights.add(new TrafficLight(
                "VW", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "VWM", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "VZ", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ"))));
        trafficlights.add(new TrafficLight(
                "VZM", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ"))));
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
