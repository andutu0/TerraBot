package terrabot.commands;

import fileio.CommandInput;
import terrabot.entities.Air.Air;
import terrabot.map.Cell;
import terrabot.map.SimMap;
import terrabot.simulation.Simulation;

public final class ChangeWeatherConditions {
    private static final int WEATHER_DURATION = 2;

    private ChangeWeatherConditions() {
    }

    /**
     * Updates the air quality based on current weather
     * @param cmd the current weather change command
     * @param simMap the current map
     * @param sim the active simulation
     * @return boolean that specifies is changes were applied on any cell
     */
    public static boolean apply(final CommandInput cmd, final SimMap simMap, final Simulation sim) {
        String weatherType = cmd.getType();
        boolean anyAffected = false;

        for (int y = 0; y < simMap.getRows(); y++) {
            for (int x = 0; x < simMap.getColumns(); x++) {
                Cell cell = simMap.getCell(x, y);
                Air air = cell.getAir();
                if (air == null) {
                    continue;
                }

                if (!Simulation.isAffectedByWeather(air, weatherType)) {
                    continue;
                }
                anyAffected = true;
                // we check for the type of weather and apply the corresponding changes
                // desert storm acts as the default branch, so i removed it
                switch (weatherType) {
                    case "peopleHiking":
                        air.computeWeatherChange((double) cmd.getNumberOfHikers());
                        break;
                    case "newSeason":
                        boolean isSpring = cmd.getSeason().equalsIgnoreCase("Spring");
                        if (isSpring) {
                            air.computeWeatherChange(null);
                        }
                        break;
                    case "polarStorm":
                        air.computeWeatherChange(cmd.getWindSpeed());
                        break;
                    case "rainfall":
                        air.computeWeatherChange(cmd.getRainfall());
                        break;
                    default:
                        air.computeWeatherChange(null);
                        break;
                }
            }
        }

        if (anyAffected) {
            sim.setWeatherActive(true);
            sim.setActiveWeatherType(weatherType);
            sim.setWeatherRevertAt(cmd.getTimestamp() + WEATHER_DURATION);
        }

        return anyAffected;
    }

}
