import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by gebruiker on 5-4-2017.
 */
public class TrafficLightsSerializer implements JsonSerializer<TrafficLights> {

    @Override
    public JsonElement serialize(final TrafficLights trafficController, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        final JsonElement jsonSTrafficLights = context.serialize(trafficController.getTrafficlights());
        jsonObject.add("trafficlights", jsonSTrafficLights);

        return jsonObject;
    }
}
