package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class DesertAir extends Air {

    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double DUST_MULTIPLIER = 0.2;
    private static final double TEMP_MULTIPLIER = 0.3;
    private static final double MAX_SCORE = 65.0;
    private static final double STORM_SCORE = 30;
    private static final double TOXICITY_SCORE_MULTIPLIER = 0.8;

    @Getter
    @Setter
    private double dustParticles;
    @Getter
    @Setter
    private boolean affectedAirQuality;

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
        double quality = normalize(score);
        setAirQuality(score);
        affectedAirQuality = false;
        return quality;
    }

    @Override
    public double computeToxicity() {
        return finalizeToxicity(MAX_SCORE, TOXICITY_SCORE_MULTIPLIER);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("desertStorm", affectedAirQuality);
    }

    @Override
    public void computeWeatherChange(final Double arg) {
        double quality = getAirQuality() - STORM_SCORE;
        affectedAirQuality = true;
        setAirQuality(quality);
    }
}
