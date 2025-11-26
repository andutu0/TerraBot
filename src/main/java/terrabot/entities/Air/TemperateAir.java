package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class TemperateAir extends Air {

    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.7;
    private static final double POLLEN_MULTIPLIER = 0.1;
    private static final double MAX_SCORE = 84.0;
    private static final double QUAL_REDUCTION = 15;
    private static final double TOXICITY_SCORE_MULTIPLIER = 0.8;


    @Getter
    @Setter
    private double pollenLevel;
    @Getter
    @Setter
    private boolean affectedAirQuality;

    public TemperateAir(final AirInput input) {
        super(input.getName(), input.getMass(), input.getType());
        setHumidity(input.getHumidity());
        setTemperature(input.getTemperature());
        setOxygenLevel(input.getOxygenLevel());
        this.pollenLevel = input.getPollenLevel();
    }

    @Override
    public double computeAirQuality() {
        double score = (getOxygenLevel() * OXYGEN_MULTIPLIER)
                + (getHumidity() * HUMIDITY_MULTIPLIER)
                - (pollenLevel * POLLEN_MULTIPLIER);
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
        node.put("pollenLevel", this.getPollenLevel());
    }

    @Override
    public void computeWeatherChange(final Double arg) {
        double quality = getAirQuality() - QUAL_REDUCTION;
        affectedAirQuality = true;
        setAirQuality(quality);
    }
}
