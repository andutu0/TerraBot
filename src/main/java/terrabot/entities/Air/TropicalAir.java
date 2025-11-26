package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class TropicalAir extends Air {

    private static final double OXYGEN_MULTIPLIER = 2.0;
    private static final double HUMIDITY_MULTIPLIER = 0.5;
    private static final double CO2_MULTIPLIER = 0.01;
    private static final double MAX = 100.0;
    private static final double MAX_SCORE = 82.0;
    private static final double RAINFALL_MULTIPLIER = 0.3;
    private static final double TOXICITY_SCORE_MULTIPLIER = 0.8;


    @Getter
    @Setter
    private double co2Level;
    @Getter
    @Setter
    private boolean affectedAirQuality;

    public TropicalAir(final AirInput input) {
        super(input.getName(), input.getMass(), input.getType());
        setHumidity(input.getHumidity());
        setTemperature(input.getTemperature());
        setOxygenLevel(input.getOxygenLevel());
        this.co2Level = Math.round(input.getCo2Level() * MAX) / MAX;
    }

    @Override
    public double computeAirQuality() {
        double score = (getOxygenLevel() * OXYGEN_MULTIPLIER)
                + (getHumidity() * HUMIDITY_MULTIPLIER)
                - (co2Level * CO2_MULTIPLIER);
        double quality = normalize(score);
        setAirQuality(quality);
        affectedAirQuality = false;
        return quality;
    }

    @Override
    public double computeToxicity() {
        return finalizeToxicity(MAX_SCORE, TOXICITY_SCORE_MULTIPLIER);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("co2Level", this.getCo2Level());
    }

    @Override
    public void computeWeatherChange(final Double arg) {
        double quality = getAirQuality() - arg * RAINFALL_MULTIPLIER;
        setAirQuality(quality);
        affectedAirQuality = true;
    }
}
