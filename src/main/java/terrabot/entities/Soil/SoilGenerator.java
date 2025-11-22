package terrabot.entities.Soil;

import fileio.SoilInput;

public final class SoilGenerator {
    private SoilGenerator() {
    }

    /**
     * Creates a Soil entity from the given SoilInput.
     *
     * @param input the input object containing soil parameters
     * @return a specific soil type
     * @throws IllegalArgumentException if the soil type is unknown
     */
    public static Soil fromInput(final SoilInput input) {
        return switch (input.getType()) {
            case "ForestSoil" -> new ForestSoil(input);
            case "SwampSoil"  -> new SwampSoil(input);
            case "TundraSoil" -> new TundraSoil(input);
            case "DesertSoil" -> new DesertSoil(input);
            default -> throw new IllegalArgumentException("Unknown soil type: " + input.getType());
        };
    }
}
