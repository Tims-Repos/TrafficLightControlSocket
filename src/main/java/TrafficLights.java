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
    @Expose(serialize = false)
    private final String[] trafficIds = {"XOZ", "XOW","XZO", "XZW", "XWO", "XWZ",
            "YNO", "YNW", "YON", "YOW", "YWN", "YWO",
            "BNW", "BZO",
            "FN", "FO", "FZ", "FW",
            "VN", "VNM", "VO", "VOM", "VZ", "VZM", "VW", "VWM"};
    @Expose(serialize = false)
    private final Map<String, List<String>> blockingIdsList = new LinkedHashMap<>();

    public void initializeTrafficLights() {
        for (String trafficId : trafficIds) {
            TrafficLight temp = new TrafficLight(trafficId);
            trafficLightMap.put(trafficId, temp);
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

    private void initializeTrafficLightBlockList() {
        //"XOZ", "XOW","XZO", "XZW", "XWO", "XWZ",
        //            "YNO", "YNW", "YON", "YOW", "YWN", "YWO",
        //            "BNW", "BZO",
        //            "FN", "FO", "FZ", "FW",
        //            "VN", "VNM", "VO", "VOM", "VZ", "VZM", "VW", "VWM"
        blockingIdsList.put(
                "XOZ", new ArrayList<>(Arrays.asList(
                        "XZW", "VZ", "VZM", "XWO", "XWZ")));
        blockingIdsList.put(
                "XOW", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "XZW")));
        blockingIdsList.put(
                "XZO", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "XWO")));
        blockingIdsList.put(
                "XZW", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "FW", "VW", "VWM", "XOZ", "XOW", "XWO")));
        blockingIdsList.put(
                "XWO", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "XZW", "XOZ", "XZO", "BZO")));
        blockingIdsList.put(
                "XWZ", new ArrayList<>(Arrays.asList(
                        "VW", "VWM", "FW", "FZ", "VZ", "VZM", "XOZ")));
        blockingIdsList.put(
                "YNO", new ArrayList<>(Arrays.asList(
                        "VN", "VNM", "FN", "FO", "VO", "VOM", "YWN", "YOW", "YWN", "YWO")));
        blockingIdsList.put(
                "YNW", new ArrayList<>(Arrays.asList(
                        "VN", "VNM", "FN", "YOW")));
        blockingIdsList.put(
                "YON", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "FN", "VN", "VNM", "YWN" )));
        blockingIdsList.put(
                "YOW", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "YWN", "BNW", "YNO", "YNW")));
        blockingIdsList.put(
                "YWN", new ArrayList<>(Arrays.asList(
                        "YNO", "YOW", "YON", "FN", "VN", "VNM")));
        blockingIdsList.put(
                "YWO", new ArrayList<>(Arrays.asList(
                        "VO", "VOM", "FO", "YNO")));
        blockingIdsList.put(
                "BNW", new ArrayList<>(Arrays.asList(
                        "FN", "VN", "VNM", "YNO", "YNW", "YOW")));
        blockingIdsList.put(
                "BZO", new ArrayList<>(Arrays.asList(
                        "VZ", "VZM", "FZ", "XZO", "XWO")));
        blockingIdsList.put(
                "FN", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN")));
        blockingIdsList.put(
                "FO", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON")));
        blockingIdsList.put(
                "FZ", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ")));
        blockingIdsList.put(
                "FW", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ")));
        blockingIdsList.put(
                "VN", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN")));
        blockingIdsList.put(
                "VNM", new ArrayList<>(Arrays.asList(
                        "YNO", "YNW", "BNW", "YON", "YWN")));
        blockingIdsList.put(
                "VO", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON")));
        blockingIdsList.put(
                "VOM", new ArrayList<>(Arrays.asList(
                        "YNO", "YWO", "YOW", "YON")));
        blockingIdsList.put(
                "VW", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ")));
        blockingIdsList.put(
                "VWM", new ArrayList<>(Arrays.asList(
                        "XOW", "XZW", "XWO", "XWZ")));
        blockingIdsList.put(
                "VZ", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ")));
        blockingIdsList.put(
                "VZM", new ArrayList<>(Arrays.asList(
                        "XZW", "XZO", "BZO", "XOZ", "XWZ")));
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
