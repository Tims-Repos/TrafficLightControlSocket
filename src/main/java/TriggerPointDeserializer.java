import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPointDeserializer implements JsonDeserializer<TriggerPoint>{

    @Override
    public TriggerPoint deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
        throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        return new TriggerPoint(
                jsonObject.get("id").getAsString(),
                jsonObject.get("type").getAsString(),
                jsonObject.get("status").getAsInt());
    }
}
