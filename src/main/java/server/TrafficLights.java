package server;

import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * Created by Timothy on 4-4-2017.
 */
public class TrafficLights {
    //The list that the server serializes to a JSON string.
    @Expose
    private List<TrafficLight> trafficlights = new ArrayList<>();
    //A map that contains the id and it's corresponding traffic light, to make
    //searching easier.
    @Expose(serialize = false)
    private final Map<String, TrafficLight> trafficLightMap = new LinkedHashMap<>();

    /**
     * Creates the traffic lights, puts them in the map and fill the
     * trafficlights list.
     */
    public void initializeTrafficLights() {
        createTrafficLights();
        for (TrafficLight trafficLight : trafficlights) {
            trafficLightMap.put(trafficLight.getId(), trafficLight);
        }
        trafficlights = new ArrayList<>(trafficLightMap.values());
    }

    /**
     * Empty traffic lights constructor.
     */
    public TrafficLights() {}

    /**
     * Searches the map with an id and returns the corresponding traffic light.
     * @param id the id to search for.
     * @return the corresponding traffic light.
     */
    public TrafficLight searchTrafficLightById(String id) {
        return trafficLightMap.get(id);
    }

    /**
     * This method is used to update the traffic light list before serialization.
     */
    public synchronized void updateTrafficLights() {
        trafficlights = new ArrayList<>(trafficLightMap.values());
    }

    /**
     * This method is used to create all the traffic lights,
     * gives them their block lists and adds them to the traffic lights list.
     */
    private void createTrafficLights() {
        final TrafficLight XOZ = new TrafficLight("XOZ");
        final TrafficLight XOW = new TrafficLight("XOW");
        final TrafficLight XZO = new TrafficLight("XZO");
        final TrafficLight XZW = new TrafficLight("XZW");
        final TrafficLight XWO = new TrafficLight("XWO");
        final TrafficLight XWZ = new TrafficLight("XWZ");

        final TrafficLight YNO = new TrafficLight("YNO");
        final TrafficLight YNW = new TrafficLight("YNW");
        final TrafficLight YON = new TrafficLight("YON");
        final TrafficLight YOW = new TrafficLight("YOW");
        final TrafficLight YWN = new TrafficLight("YWN");
        final TrafficLight YWO = new TrafficLight("YWO");

        final TrafficLight BNW = new TrafficLight("BNW");
        final TrafficLight BZO = new TrafficLight("BZO");

        final TrafficLight FN = new TrafficLight("FN");
        final TrafficLight FO = new TrafficLight("FO");
        final TrafficLight FZ = new TrafficLight("FZ");
        final TrafficLight FW = new TrafficLight("FW");

        final TrafficLight VN = new TrafficLight("VN");
        final TrafficLight VNM = new TrafficLight("VNM");
        final TrafficLight VO = new TrafficLight("VO");
        final TrafficLight VOM = new TrafficLight("VOM");
        final TrafficLight VZ = new TrafficLight("VZ");
        final TrafficLight VZM = new TrafficLight("VZM");
        final TrafficLight VW = new TrafficLight("VW");
        final TrafficLight VWM = new TrafficLight("VWM");


        XOZ.setBlockList(new ArrayList<>(Arrays.asList(
                XZW, VZ, VZM, XWO, XWZ)));
        XOW.setBlockList(new ArrayList<>(Arrays.asList(
                VW, VWM, FW, XZW)));
        XZO.setBlockList(new ArrayList<>(Arrays.asList(
                VZ, VZM, FZ, XWO)));
        XZW.setBlockList(new ArrayList<>(Arrays.asList(
                VZ, VZM, FZ, FW, VW, VWM, XOZ, XOW, XWO)));
        XWO.setBlockList(new ArrayList<>(Arrays.asList(
                VW, VWM, FW, XZW, XOZ, XZO, BZO)));
        XWZ.setBlockList(new ArrayList<>(Arrays.asList(
                VW, VWM, FW, FZ, VZ, VZM, XOZ)));

        YNO.setBlockList(new ArrayList<>(Arrays.asList(
                VN, VNM, FN, FO, VO, VOM, YWN, YOW, YWN, YWO)));
        YNW.setBlockList(new ArrayList<>(Arrays.asList(
                VN, VNM, FN, YOW)));
        YON.setBlockList(new ArrayList<>(Arrays.asList(
                VO, VOM, FO, FN, VN, VNM, YWN )));
        YOW.setBlockList(new ArrayList<>(Arrays.asList(
                VO, VOM, FO, YWN, BNW, YNO, YNW)));
        YWN.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YOW, YON, FN, VN, VNM)));
        YWO.setBlockList(new ArrayList<>(Arrays.asList(
                VO, VOM, FO, YNO)));

        BNW.setBlockList(new ArrayList<>(Arrays.asList(
                FN, VN, VNM, YNO, YNW, YOW)));
        BZO.setBlockList(new ArrayList<>(Arrays.asList(
                VZ, VZM, FZ, XZO, XWO)));

        FN.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YNW, BNW, YON, YWN)));
        FO.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YWO, YOW, YON)));
        FZ.setBlockList(new ArrayList<>(Arrays.asList(
                XZW, XZO, BZO, XOZ, XWZ)));
        FW.setBlockList(new ArrayList<>(Arrays.asList(
                XOW, XZW, XWO, XWZ)));

        VN.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YNW, BNW, YON, YWN)));
        VNM.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YNW, BNW, YON, YWN)));
        VO.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YWO, YOW, YON)));
        VOM.setBlockList(new ArrayList<>(Arrays.asList(
                YNO, YWO, YOW, YON)));
        VZ.setBlockList(new ArrayList<>(Arrays.asList(
                XZW, XZO, BZO, XOZ, XWZ)));
        VZM.setBlockList(new ArrayList<>(Arrays.asList(
                XZW, XZO, BZO, XOZ, XWZ)));
        VW.setBlockList(new ArrayList<>(Arrays.asList(
                XOW, XZW, XWO, XWZ)));
        VWM.setBlockList(new ArrayList<>(Arrays.asList(
                XOW, XZW, XWO, XWZ)));

        trafficlights.add(XOZ);
        trafficlights.add(XOW);
        trafficlights.add(XZO);
        trafficlights.add(XZW);
        trafficlights.add(XWO);
        trafficlights.add(XWZ);

        trafficlights.add(YNO);
        trafficlights.add(YNW);
        trafficlights.add(YON);
        trafficlights.add(YOW);
        trafficlights.add(YWN);
        trafficlights.add(YWO);

        trafficlights.add(BNW);
        trafficlights.add(BZO);

        trafficlights.add(FN);
        trafficlights.add(FO);
        trafficlights.add(FZ);
        trafficlights.add(FW);

        trafficlights.add(VN);
        trafficlights.add(VNM);
        trafficlights.add(VO);
        trafficlights.add(VOM);
        trafficlights.add(VZ);
        trafficlights.add(VZM);
        trafficlights.add(VW);
        trafficlights.add(VWM);

    }

    /**
     * @return the string representation of the trafficlights object.
     */
    @Override
    public String toString(){
        final StringBuilder formatted = new StringBuilder();
        for (final TrafficLight trafficLight : trafficlights) {
            formatted.append("\n >> ").append(trafficLight);

        }

        return formatted.toString();
    }

}
