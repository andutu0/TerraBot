package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class DesertAir extends Air {

    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double DUST_MULTIPLIER = 0.2;
    private static final double TEMP_MULTIPLIER = 0.3;
    private static final double MAX = 100.0;
    private static final double TOXICITY_MULTIPLIER = 100.0;
    private static final double MAX_SCORE = 65.0;

    @Getter
    @Setter
    private double dustParticles;

    public DesertAir(final AirInput input) {
        super(input.getName(), input.getMass(), input.getType());
        setHumidity(input.getHumidity());
        setTemperature(input.getTemperature());
        setOxygenLevel(input.getOxygenLevel());
        this.dustParticles = input.getDustParticles();
    }

    @Override
    public double computeAirQuality() {
        double score = (getOxygenLevel() * OXYGEN_MULTIPLIER)
                - (dustParticles * DUST_MULTIPLIER)
                - (getTemperature() * TEMP_MULTIPLIER);
        return normalize(score);
    }

    @Override
    public double computeToxicity() {
        double aq = computeAirQuality();
        double toxicity = TOXICITY_MULTIPLIER * (1 - aq / MAX_SCORE);
        // 100 is not a magic number intellij, its literally %
        return Math.round(toxicity * MAX) / MAX;
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("dustParticles", this.dustParticles);
    }
}
