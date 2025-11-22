package terrabot.entities.Air;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import fileio.AirInput;

public final class MountainAir extends Air {

    private static final double ALTITUDE_DIVISOR = 1000.0;
    private static final double ALTITUDE_MULTIPLIER = 0.5;
    private static final double HUMIDITY_MULTIPLIER = 0.6;
    private static final double MAX = 100.0;
    private static final double TOXICITY_MULTIPLIER = 100.0;
    private static final double MAX_SCORE = 78.0;
    private static final double HIKER_MULTIPLIER = 0.1;

    @Getter
    @Setter
    private double altitude;
    @Getter
    @Setter
    private boolean affectedAirQuality;

    public MountainAir(final AirInput input) {
        super(input.getName(), input.getMass(), input.getType());
        setHumidity(input.getHumidity());
        setTemperature(input.getTemperature());
        setOxygenLevel(input.getOxygenLevel());
        this.altitude = input.getAltitude();
    }

    @Override
    public double computeAirQuality() {
        double oxygenFactor = getOxygenLevel()
                - (altitude / ALTITUDE_DIVISOR * ALTITUDE_MULTIPLIER);
        double score = (oxygenFactor * 2) + (getHumidity() * HUMIDITY_MULTIPLIER);
        double quality = normalize(score);
        setAirQuality(quality);
        affectedAirQuality = false;
        return quality;
    }

    @Override
    public double computeToxicity() {
        double aq = computeAirQuality();
        double toxicity = TOXICITY_MULTIPLIER * (1 - aq / MAX_SCORE);
        return Math.round(toxicity * MAX) / MAX;
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("altitude", this.getAltitude());
    }

    @Override
    public double computeWeatherChange(final Double arg) {
        affectedAirQuality = true;
        setAirQuality(computeAirQuality() - arg * HUMIDITY_MULTIPLIER);
        return (computeAirQuality() - arg * HUMIDITY_MULTIPLIER);
    }
}
