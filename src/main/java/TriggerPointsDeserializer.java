import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPointsDeserializer implements JsonDeserializer<TriggerPoints> {

    @Override
    public TriggerPoints deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
        throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final TriggerPoint[] triggerPointArray = context.deserialize(jsonObject.get("triggerpoints"), TriggerPoint[].class);
        final List<TriggerPoint> triggerPointList = Arrays.asList(triggerPointArray);
        final TriggerPoints triggerPoints = new TriggerPoints();
        triggerPoints.setTriggerpoints(triggerPointList);
        return triggerPoints;
    }
}
