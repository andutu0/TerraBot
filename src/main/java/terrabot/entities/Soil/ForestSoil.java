package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import lombok.Setter;

public final class ForestSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 1.2;
    private static final double ORGANIC_MATTER_MULTIPLIER = 2;
    private static final double WATER_RETENTION_MULTIPLIER = 1.5;
    private static final double LEAF_LITTER_MULTIPLIER = 0.3;
    @Getter @Setter
    private double leafLitter;

    public ForestSoil(final SoilInput input) {
        super(input.getName(), input.getMass(), input.getType());
        this.setNitrogen(input.getNitrogen());
        this.setWaterRetention(input.getWaterRetention());
        this.setSoilpH(input.getSoilpH());
        this.setOrganicMatter(input.getOrganicMatter());
        this.leafLitter = input.getLeafLitter();
    }

    @Override
    public double computeSoilQuality() {
        double quality = (getNitrogen() * NITROGEN_MULTIPLIER)
                        + (getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                        + (getWaterRetention() * WATER_RETENTION_MULTIPLIER)
                        + (leafLitter * LEAF_LITTER_MULTIPLIER);
        return normalize(quality);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("leafLitter", this.getLeafLitter());
    }
}
