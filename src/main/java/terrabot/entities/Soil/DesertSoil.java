package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import lombok.Setter;

public final class DesertSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 0.5;
    private static final double WATER_RETENTION_MULTIPLIER = 0.3;
    private static final int MAX = 100;
    @Getter @Setter
    private double salinity;

    public DesertSoil(final SoilInput input) {
        super(input.getName(),  input.getMass(), input.getType());
        this.setNitrogen(input.getNitrogen());
        this.setWaterRetention(input.getWaterRetention());
        this.setSoilpH(input.getSoilpH());
        this.setOrganicMatter(input.getOrganicMatter());
        this.salinity = input.getSalinity();
    }

    @Override
    public double computeStuckChance() {
        return ((MAX - getWaterRetention() + salinity) / MAX * MAX);
    }

    @Override
    public double computeSoilQuality() {
        double quality = (getNitrogen() * NITROGEN_MULTIPLIER)
                        + (getWaterRetention() * WATER_RETENTION_MULTIPLIER)
                        - (salinity * 2);
        return normalize(quality);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("salinity", this.getSalinity());
    }
}
