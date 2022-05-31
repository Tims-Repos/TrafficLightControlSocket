import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
/**
 * Created by gebruiker on 6-4-2017.
 */
public class TrafficLightSerializer implements JsonSerializer<TrafficLight> {

    @Override
    public JsonElement serialize(final TrafficLight trafficlight, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", trafficlight.getId());
        jsonObject.addProperty("status", trafficlight.getStatus());

        return jsonObject;
    }
}
