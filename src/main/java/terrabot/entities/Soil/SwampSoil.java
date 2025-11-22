package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import lombok.Setter;

public final class SwampSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.1;
    private static final double ORGANIC_MATTER_MULTIPLIER = 2.2;
    private static final double WATER_LOGGING_MULTIPLIER = 5;

    @Getter @Setter
    private double waterLogging;

    public SwampSoil(final SoilInput input) {
        super(input.getName(), input.getMass(), input.getType());
        this.setNitrogen(input.getNitrogen());
        this.setWaterRetention(input.getWaterRetention());
        this.setSoilpH(input.getSoilpH());
        this.setOrganicMatter(input.getOrganicMatter());
        this.waterLogging = input.getWaterLogging();
    }

    @Override
    public double computeSoilQuality() {
        double quality = (getNitrogen() * NITROGEN_MULTIPLIER)
                        + (getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                        - (waterLogging * WATER_LOGGING_MULTIPLIER);
        return normalize(quality);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("waterLogging", this.getWaterLogging());
    }
}
