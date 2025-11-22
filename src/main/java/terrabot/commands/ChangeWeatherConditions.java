package terrabot.commands;

import fileio.CommandInput;
import terrabot.entities.Air.Air;
import terrabot.map.Cell;
import terrabot.map.Map;
import terrabot.simulation.Simulation;

public final class ChangeWeatherConditions {
    private static final int WEATHER_DURATION = 2;

    private ChangeWeatherConditions() {
    }

    /**
     * Updates the air quality based on current weather
     * @param cmd the current weather change command
     * @param map the current map
     * @param sim the active simulation
     * @return boolean that specifies is changes were applied on any cell
     */
    public static boolean apply(final CommandInput cmd, final Map map, final Simulation sim) {
        String weatherType = cmd.getType();
        boolean anyAffected = false;

        for (int y = 0; y < map.getRows(); y++) {
            for (int x = 0; x < map.getColumns(); x++) {
                Cell cell = map.getCell(x, y);
                Air air = cell.getAir();
                if (air == null) {
                    continue;
                }

                if (!Simulation.isAffectedByWeather(air, weatherType)) {
                    continue;
                }
                anyAffected = true;
                switch (weatherType) {
                    case "desertStorm":
                        air.computeWeatherChange(null);
                        break;
                    case "peopleHiking":
                        air.computeWeatherChange((double) cmd.getNumberOfHikers());
                        break;
                    case "newSeason":
                        air.computeWeatherChange(null);
                        break;
                    case "polarStorm":
                        air.computeWeatherChange(null);
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
