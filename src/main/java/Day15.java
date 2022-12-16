import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Day15 extends BaseDay {
    private final static String inputFile = "day15.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day15();
        day.run(getInput(day.getClass(), false));
    }

    public void run(String input) throws URISyntaxException, IOException {
        final var grid = new Grid();
        for (var line : input.lines().toList()) {
            final var lineSplit = line.split(" ");

            final var sensorCoord = new Coord(extractNumber(lineSplit[2]), extractNumber(lineSplit[3]));
            final var beaconCoord = new Coord(extractNumber(lineSplit[8]), extractNumber(lineSplit[9]));
            grid.sensors.add(new Sensor(sensorCoord, beaconCoord));
        }

        final var minX = grid.sensors.stream()
                .map(Sensor::getMinXRange)
                .min(Comparator.naturalOrder()).orElse(0);

        final var maxX = grid.sensors.stream()
                .map(Sensor::getMaxXRange)
                .max(Comparator.naturalOrder()).orElse(0);

        final var searchY = 10;
        var coveredArea = 0L;
        for (int x = minX; x < maxX; x++) {
            if (grid.isCovered(x, searchY)) {
                coveredArea++;
            }
        }
        log.info("Results: {}", coveredArea);

        final var uncovered = new HashSet<Coord>();
        for (var sensor : grid.sensors) {
            uncovered.addAll(
                    sensor.getBorderedCoords().stream()
                            .filter(it -> !grid.isCovered(it.x, it.y))
                            .toList());
        }
        for (var u : uncovered) {
            if (u.x >= 0 && u.x <= 4000000 && u.y >= 0 && u.y <= 4000000) {
                log.info("Results: {}", u);
            }
        }
    }

    private int extractNumber(String string) {
        final var split = string.split("=");
        final var sanitizedString = split[1]
                .replaceAll(",", "").replaceAll(":", "");
        return Integer.parseInt(sanitizedString);
    }

    public static class Grid {
        List<Sensor> sensors = new ArrayList<>();

        Set<Coord> getCoveredArea(Integer yRow) {
            final var area = new HashSet<Coord>();
            final var sensorLocations = sensors.stream()
                    .map(it -> it.closestBeacon).collect(Collectors.toCollection(HashSet::new));

            for (var sensor : sensors) {
                area.addAll(sensor.getCoveredCoords(yRow));
            }

            return area.stream()
                    .filter(it -> !sensorLocations.contains(it)).collect(Collectors.toSet());
        }

        long coveredAreaOfY(int y) {
            return sensors.stream()
                    .map(it -> it.getCoordsCoveringY(y))
                    .reduce(0L, Long::sum);
        }

        boolean isCovered(int x, int y) {
            final var currentCoord = new Coord(x, y);
            final var isBeacon = sensors.stream()
                    .anyMatch(it -> it.closestBeacon.equals(currentCoord));

            final var isSensor = sensors.stream()
                    .anyMatch(it -> it.coord.equals(currentCoord));

            if (isBeacon) {
                return false;
            }

            return sensors.stream()
                    .anyMatch(it -> it.isInRange(currentCoord));

        }
    }

    @AllArgsConstructor
    public static class Sensor {
        Coord coord;
        Coord closestBeacon;

        private int getAbsDistance() {
            final var absX = Math.abs(coord.x - closestBeacon.x);
            final var absY = Math.abs(coord.y - closestBeacon.y);
            return absX + absY;
        }

        private int getMinXRange() {
            return coord.x - getAbsDistance();
        }

        private int getMaxXRange() {
            return coord.x + getAbsDistance();
        }

        public Set<Coord> getCoveredCoords(Integer yRow) {
            final var coords = new HashSet<Coord>();
            final var absDistance = getAbsDistance();

            for (int x = -absDistance; x <= absDistance; x++) {
                final var yRange = x < 0 ? x + absDistance : absDistance - x;
                for (int y = -yRange; y <= yRange; y++) {
                    final var currentCoord = new Coord(coord.x + x, coord.y + y);
                    if (yRow == null || currentCoord.y == yRow) {
                        coords.add(currentCoord);
                    }
                }
            }

            return coords;
        }

        public Set<Coord> getBorderedCoords() {
            final var coords = new HashSet<Coord>();
            final var absDistance = getAbsDistance();

            final var maxDistance = absDistance + 1;

            for (int x = -maxDistance; x <= maxDistance; x++) {
                final var yRange = x < 0 ? x + maxDistance : maxDistance - x;
                coords.add(new Coord(coord.x + x, coord.y - yRange));
                coords.add(new Coord(coord.x + x, coord.y + yRange));
            }

            return coords;
        }

        public long getCoordsCoveringY(int y) {
            return getCoveredCoords(y).stream()
                    .filter(it -> it.y == y)
                    .count();
        }

        public boolean isInRange(Coord other) {
            final var absDistance = getAbsDistance();

            final var xDelta = Math.abs(other.x - coord.x);
            final var yDelta = Math.abs(other.y - coord.y);

            return xDelta + yDelta <= absDistance;
        }
    }

    public record Coord(int x, int y) {

    }
}
