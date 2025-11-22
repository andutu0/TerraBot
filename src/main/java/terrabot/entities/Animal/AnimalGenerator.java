package terrabot.entities.Animal;

import fileio.AnimalInput;

public final class AnimalGenerator {

    private AnimalGenerator() {
    }

    /**
     * Creates an Animal entity from the given AnimalInput.
     *
     * @param input the input object containing animal parameters
     * @return a specific animal type
     * @throws IllegalArgumentException if the animal type is unknown
     */
    public static Animal fromInput(final AnimalInput input) {
        return switch (input.getType()) {
            case "Carnivores"  -> new Carnivore(input);
            case "Omnivores" -> new Omnivore(input);
            case "Herbivores"  -> new Herbivore(input);
            case "Detritivores"  -> new Detritivore(input);
            case "Parasites"  -> new Parasite(input);
            default -> throw new IllegalArgumentException(
                    "Unknown animal type: " + input.getType()
            );
        };
    }
}
