package terrabot.entities.Air;

import fileio.AirInput;

public final class AirGenerator {

    private AirGenerator() {
    }

    /**
     * Creates an Air entity from the given AirInput.
     *
     * @param input the input object containing air parameters
     * @return a specific Air type
     * @throws IllegalArgumentException if the air type is unknown
     */
    public static Air fromInput(final AirInput input) {
        return switch (input.getType()) {
            case "MountainAir"  -> new MountainAir(input);
            case "TemperateAir" -> new TemperateAir(input);
            case "TropicalAir"  -> new TropicalAir(input);
            default -> throw new IllegalArgumentException(
                    "Unknown air type: " + input.getType()
            );
        };
    }
}
