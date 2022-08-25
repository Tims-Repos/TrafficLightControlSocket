package server;

import java.util.List;

/**
 * Created by Timothy on 4-4-2017.
 */
public class TriggerPoints {
    /**
     * The list to which the JSON string received from the client
     * gets deserialized.
     */
    private List<TriggerPoint> triggerpoints = null;

    /**
     * @return The list of triggerpoints.
     */
    public List<TriggerPoint> getTriggerpoints() {
        return triggerpoints;
    }

    /**
     * @return The string representation of the triggerpoints object.
     */
    @Override
    public String toString(){
        final StringBuilder formatted = new StringBuilder();
        for (final TriggerPoint triggerpoint : triggerpoints) {
            formatted.append("\n >> ").append(triggerpoint);

        }
        return formatted.toString();

    }
}
