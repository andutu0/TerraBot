package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class PolarAir extends Air {

    private static final int BASE_TEMP = 100;
    private static final double ICE_MULTIPLIER = 0.05;
    private static final int POLAR_MAX_SCORE = 142;
    private static final double WEATHER_MULTIPLIER = 0.2;
    private static final double TOXICITY_SCORE_MULTIPLIER = 0.8;

    @Getter @Setter
    private double iceCrystalConcentration;
    @Getter @Setter
    private boolean affectedAirQuality;

    public PolarAir(final AirInput input) {
        super(input.getName(), input.getMass(), input.getType());
        setHumidity(input.getHumidity());
        setTemperature(input.getTemperature());
        setOxygenLevel(input.getOxygenLevel());
        this.iceCrystalConcentration = input.getIceCrystalConcentration();
    }

    @Override
    public double computeAirQuality() {
        double score = (getOxygenLevel() * 2) + (BASE_TEMP - Math.abs(getTemperature()))
                        - (iceCrystalConcentration * ICE_MULTIPLIER);
        double quality = normalize(score);
        setAirQuality(score);
        return quality;
    }

    @Override
    public double computeToxicity() {
        return finalizeToxicity(POLAR_MAX_SCORE, TOXICITY_SCORE_MULTIPLIER);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("iceCrystalConcentration", this.iceCrystalConcentration);
    }

    @Override
    public void computeWeatherChange(final Double arg) {
        setAffectedAirQuality(true);
        double quality = getAirQuality() - WEATHER_MULTIPLIER * arg;
        setAirQuality(quality);
    }
}
