import java.util.List;

/**
 * Created by gebruiker on 4-4-2017.
 */
public class TriggerPoints {

    private List<TriggerPoint> triggerpoints = null;

    public List<TriggerPoint> getTriggerpoints() {
        return triggerpoints;
    }

    public void setTriggerpoints(List<TriggerPoint> triggerpoints) {
        this.triggerpoints = triggerpoints;
    }

    @Override
    public String toString(){
        final StringBuilder formatted = new StringBuilder();
        for (final TriggerPoint triggerpoint : triggerpoints) {
            formatted.append("\n >> ").append(triggerpoint);

    }
        return formatted.toString();

    }
}
