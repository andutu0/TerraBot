package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

public abstract class Soil extends Entity {

    private static final int MAX_SCORE = 100;
    private static final double ROUND_FACTOR = 100.0;
    private static final double DECIMAL_ROUND = 10.0;

    @Getter
    @Setter
    private double nitrogen;

    private double waterRetention;

    @Getter
    @Setter
    private double soilpH;

    @Getter
    @Setter
    private double organicMatter;

    @Getter
    @Setter
    private String type;

    public Soil(final String name, final double mass, final String type) {
        super(name, mass);
        this.type = type;
    }

    /**
     * Returns the waterRetention level rounded to 1 decimal.
     */
    public double getWaterRetention() {
        return Math.round(waterRetention * ROUND_FACTOR) / ROUND_FACTOR;
    }

    /**
     * Sets the humidity rounded to 1 decimal.
     */
    public void setWaterRetention(final double value) {
        this.waterRetention = Math.round(value * ROUND_FACTOR) / ROUND_FACTOR;
    }

    /**
     * Computes the chance to get stuck in a specific type of soil.
     *
     * @return the change to get stuck.
     */
    public abstract double computeStuckChance();
    /**
     * Computes the soil quality using the formula specific to each soil type.
     *
     * @return the normalized soil quality score in the range [0, 100]
     */
    public abstract double computeSoilQuality();

    /**
     * Adds soil-type-specific fields to the output JSON node
     *
     * @param node the JSON node to enrich
     */
    public abstract void addSpecificFields(ObjectNode node);

    protected final double normalize(final double score) {
        double clamped = Math.max(0, Math.min(MAX_SCORE, score));
        return Math.round(clamped * ROUND_FACTOR) / ROUND_FACTOR;
    }
}
