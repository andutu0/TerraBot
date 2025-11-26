package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import lombok.Setter;

public final class GrasslandSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.3;
    private static final double ORGANIC_MATTER_MULTIPLIER = 1.5;
    private static final double ROOT_DENSITY_MULTIPLIER = 0.8;
    private static final int ROOT_DENSITY_BASE = 50;
    private static final double WATER_RETENTION_MULTIPLIER = 0.5;
    private static final int WATER_RETENTION_DIVIDER = 75;
    private static final int MAX = 100;
    @Getter @Setter
    private double rootDensity;

    public GrasslandSoil(final SoilInput input) {
        super(input.getName(), input.getMass(), input.getType());
        this.setNitrogen(input.getNitrogen());
        this.setWaterRetention(input.getWaterRetention());
        this.setSoilpH(input.getSoilpH());
        this.setOrganicMatter(input.getOrganicMatter());
        this.rootDensity = input.getRootDensity();
    }

    @Override
    public double computeStuckChance() {
        return (((ROOT_DENSITY_BASE - rootDensity) + getWaterRetention()
                * WATER_RETENTION_MULTIPLIER) / WATER_RETENTION_DIVIDER * MAX);
    }

    @Override
    public double computeSoilQuality() {
        double quality = (getNitrogen() * NITROGEN_MULTIPLIER)
                        + (getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                        + (rootDensity * ROOT_DENSITY_MULTIPLIER);
        return normalize(quality);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("rootDensity", rootDensity);
    }
}
