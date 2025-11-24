package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

public abstract class Air extends Entity {
    private static final int MAX_SCORE = 100;
    private static final double ROUND_FACTOR = 100.0;
    private static final double DECIMAL_ROUND = 10.0;
    @Getter
    @Setter
    private double humidity;
    @Getter
    @Setter
    private double temperature;
    private double oxygenLevel;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private double airQuality;

    public Air(final String name, final double mass, final String type) {
        super(name, mass);
        this.type = type;
    }

    /**
     * Returns the oxygen level rounded to 1 decimal.
     * Subclasses should not override this method.
     */
    public double getOxygenLevel() {
        return Math.round(oxygenLevel * DECIMAL_ROUND) / DECIMAL_ROUND;
    }

    /**
     * Sets the oxygen level rounded to 1 decimal.
     * Subclasses should not override this method.
     */
    public void setOxygenLevel(final double value) {
        this.oxygenLevel = Math.round(value * DECIMAL_ROUND) / DECIMAL_ROUND;
    }

    /**
     * Computes the air quality score for this air type.
     *
     * @return a score in the range [0, 100]
     */
    public abstract double computeAirQuality();

    /**
     * Computes the toxicity level for this air type.
     *
     * @return toxicity level
     */
    public abstract double computeToxicity();

    /**
     * Adds type-specific fields (e.g. altitude, pollenLevel) to the given node.
     *
     * @param node the JSON object node to be enriched
     */
    public abstract void addSpecificFields(ObjectNode node);

    /**
     * Computes the air quality score for this air type after
     * some weather conditions.
     *
     * @return a score in the range [0, 100]
     */
    public abstract void computeWeatherChange(Double arg);

    protected final double normalize(final double score) {
        double clamped = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(clamped * ROUND_FACTOR) / ROUND_FACTOR;
    }
}
