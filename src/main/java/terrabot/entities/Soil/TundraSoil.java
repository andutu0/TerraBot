package terrabot.entities.Soil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.SoilInput;
import lombok.Getter;
import lombok.Setter;

public final class TundraSoil extends Soil {
    private static final double NITROGEN_MULTIPLIER = 0.7;
    private static final double ORGANIC_MATTER_MULTIPLIER = 0.5;
    private static final double PERMA_FROST_MULTIPLIER = 1.5;
    @Getter @Setter
    private double permafrostDepth;

    public TundraSoil(final SoilInput input) {
        super(input.getName(), input.getMass(), input.getType());
        this.setNitrogen(input.getNitrogen());
        this.setWaterRetention(input.getWaterRetention());
        this.setSoilpH(input.getSoilpH());
        this.setOrganicMatter(input.getOrganicMatter());
        this.permafrostDepth = input.getPermafrostDepth();
    }

    @Override
    public double computeSoilQuality() {
        double quality = (getNitrogen() * NITROGEN_MULTIPLIER)
                        + (getOrganicMatter() * ORGANIC_MATTER_MULTIPLIER)
                        - (permafrostDepth * PERMA_FROST_MULTIPLIER);
        return normalize(quality);
    }

    @Override
    public void addSpecificFields(final ObjectNode node) {
        node.put("permafrostDepth", this.getPermafrostDepth());
    }
}
