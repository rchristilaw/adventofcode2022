import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class Day14 extends BaseDay {
    private final static String inputFile = "day14.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day14();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().toList();

        final var grid = new Grid();
        input.forEach(it -> grid.paths.addAll(buildPaths(it.split(" -> "))));

        var sandCoord = new Coord(500, 0);
        var sandCount = 0;

        do {
            if (grid.isSpaceOccupied(sandCoord.x, sandCoord.y + 1)) {
                if (grid.isSpaceOccupied(sandCoord.x - 1, sandCoord.y + 1)) {
                    if (grid.isSpaceOccupied(sandCoord.x + 1, sandCoord.y + 1)) {
                        sandCount++;
                        if (sandCoord.x == 500 && sandCoord.y == 0) {
                            break;
                        }
                        grid.sand.add(sandCoord);
                        sandCoord = new Coord(500, 0);
                    } else {
                        sandCoord.y++;
                        sandCoord.x++;
                    }
                } else {
                    sandCoord.y++;
                    sandCoord.x--;
                }
            } else {
                sandCoord.y++;
            }

        } while (sandCoord.y <= 300);

        log.info("Results: {}", sandCount);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }

    @RequiredArgsConstructor
    public static class Grid {
        private final List<Coord> paths = new ArrayList<Coord>();
        List<Coord> sand = new ArrayList<Coord>();

        private Integer maxY = null;

        boolean isSpaceOccupied(int x, int y) {
            if (maxY == null) {
                maxY = findMaxY() + 2;
            }

            // ADDED for PART 2
            if (y == maxY) {
                return true;
            }

            for (var coord : paths) {
                if (coord.x == x && coord.y == y) {
                    return true;
                }
            }
            for (var coord : sand) {
                if (coord.x == x && coord.y == y) {
                    return true;
                }
            }
            return false;
        }

        int findMaxY() {
            return paths.stream()
                .map(it -> it.y).min(Comparator.reverseOrder()).orElse(0);
        }
    }

    private List<Coord> buildPaths(String[] coordsArr) {
        final var corners = Arrays.stream(coordsArr)
            .map(it -> {
                final var val = it.split(",");
                return new Coord(Integer.parseInt(val[0]), Integer.parseInt(val[1]));
            })
            .toList();

        final var coords = new ArrayList<Coord>();
        for (int i = 0; i < corners.size() - 1; i++) {
            coords.addAll(connectCoords(corners.get(i), corners.get(i+1)));
        }

        return coords;
    }
    private List<Coord> connectCoords(Coord c1, Coord c2) {
        final var coords = new ArrayList<Coord>();
        if (c1.x != c2.x && c1.y != c2.y) {
            throw new RuntimeException("Is this allowed");
        } else if (c1.x < c2.x) {
            for (int x = c1.x; x <= c2.x; x++) {
                coords.add(new Coord(x, c1.y));
            }
        } else if (c2.x < c1.x) {
            for (int x = c2.x; x <= c1.x; x++) {
                coords.add(new Coord(x, c1.y));
            }
        } else if (c1.y < c2.y) {
            for (int y = c1.y; y <= c2.y; y++) {
                coords.add(new Coord(c1.x, y));
            }
        } else if (c2.y < c1.y) {
            for (int y = c2.y; y <= c1.y; y++) {
                coords.add(new Coord(c1.x, y));
            }
        }
        return coords;
    }

    @AllArgsConstructor
    public static class Coord {
        int x;
        int y;

        public boolean equals(Coord other) {
            return x == other.x && y == other.y;
        }
    }
}
