package terrabot.entities.Water;

import fileio.WaterInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

import static java.lang.Math.abs;

// fara subclase pentru ca nu folosim tipurile la nimic

public final class Water extends Entity {
    private static final double PERCENT_DIV = 100.0;
    private static final double SEVEN_P_FIVE_DIV = 7.5;
    private static final double SALINITY_DIV = 350;
    private static final int GOOD_BENCHMARK = 70;
    private static final int MEDIUM_BENCHMARK = 40;
    // NU MAI SUPORT WARNINGURILE DE MAGIC NUMBER
    private static final double FIRST_MULTIPLIER = 0.1;
    private static final double SECOND_MULTIPLIER = 0.15;
    private static final double THIRD_MULTIPLIER = 0.2;
    private static final double FOURTH_MULTIPLIER = 0.3;
    private static final int GOOD_ID = 3;
    private static final int MEDIUM_ID = 2;
    private static final int BAD_ID = 1;

    @Getter @Setter
    private double salinity;
    @Getter @Setter
    private double pH;
    @Getter @Setter
    private double purity;
    @Getter @Setter
    private double turbidity;
    @Getter @Setter
    private double contaminantIndex;
    @Getter @Setter
    private boolean isFrozen;
    @Getter @Setter
    private String type;

    public Water(final WaterInput input) {
        super(input.getName(), input.getMass());
        this.salinity = input.getSalinity();
        this.purity = input.getPurity();
        this.turbidity = input.getTurbidity();
        this.contaminantIndex = input.getContaminantIndex();
        this.isFrozen = input.isFrozen();
        this.type = input.getType();
    }

    // 1 == Poor, 2 == Moderate, 3 == Good
    /**
     * Computes the water quality.
     *
     * @return the water quality of a specific cell
     */
    public int getWaterQuality() {
        double purityScore        = purity / PERCENT_DIV;
        double pHScore            = 1 - abs(pH - SEVEN_P_FIVE_DIV) / SEVEN_P_FIVE_DIV;
        double salinityScore        = 1 - (salinity / SALINITY_DIV);
        double turbidityScore      = 1 - (turbidity / PERCENT_DIV);
        double contaminantScore     = 1 - (contaminantIndex / PERCENT_DIV);
        double frozenScore          = isFrozen ? 0 : 1;

        double waterQuality = (FOURTH_MULTIPLIER * purityScore + THIRD_MULTIPLIER * pHScore
                        + SECOND_MULTIPLIER * salinityScore
                        + FIRST_MULTIPLIER * turbidityScore
                        + SECOND_MULTIPLIER * contaminantScore
                        + THIRD_MULTIPLIER * frozenScore) * PERCENT_DIV;

        if (waterQuality >= GOOD_BENCHMARK) {
            return GOOD_ID;
        } else if (waterQuality >= MEDIUM_BENCHMARK) {
            return MEDIUM_ID;
        } else {
            return BAD_ID;
        }
    }
}
