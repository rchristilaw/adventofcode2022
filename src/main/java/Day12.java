import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day12 extends BaseDay {

    private final static String inputFile = "day12.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day12();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {

        final var lines = readClassPathResource(inputFile).lines().toList();

        Grid grid = new Grid();
        for (int y = 0; y < lines.size(); y++) {
            grid.addRow(lines.get(y));
        }

        final var start = grid.findStart();
        final var end = grid.findEnd();

        int distance = getShortestTrip(grid, start, end);
        log.info("Results: {}", distance);

        int bestDist = distance;
        for (var currentStart : grid.findACoords()) {
            bestDist = Math.min(bestDist, getShortestTrip(grid, currentStart, end));
        }
        log.info("Results: {}", bestDist);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }

    private int getShortestTrip(Grid grid, Coord start, Coord end) {
        grid.resetPos();
        start.distance = 0;
        final var queue = new PriorityQueue<Coord>();
        queue.add(start);
        while (!queue.isEmpty()) {
            final var cur = queue.remove();

            if (cur.row == end.row && cur.col == end.col) {
                return cur.distance;
            }

            navigate(cur, cur.row, cur.col - 1, grid, queue);
            navigate(cur, cur.row, cur.col + 1, grid, queue);
            navigate(cur, cur.row - 1, cur.col, grid, queue);
            navigate(cur, cur.row + 1, cur.col, grid, queue);
        }
        return Integer.MAX_VALUE;
    }

    private void navigate(Coord cur, int row, int col, Grid grid, PriorityQueue<Coord> queue) {
        if (row == -1 || col == -1 || row == grid.rowCount() || col == grid.colCount()) {
            return;
        }
        final var nextCoord = grid.get(row, col);
        if (nextCoord.elevation - cur.elevation > 1) {
            return;
        }
        int pathLen = cur.distance + 1;
        if (pathLen < nextCoord.distance) {
            nextCoord.distance = pathLen;
            queue.remove(nextCoord);
            queue.add(nextCoord);
        }
    }

    private static class Grid {
        private final List<List<Coord>> grid = new ArrayList<>();
        private Coord start;
        private Coord end;

        public void addRow(String s) {
            final var line = s.split("");

            final var listX = new ArrayList<Coord>();
            for (int x = 0; x < line.length; x++) {
                final var currentChar = line[x].charAt(0);
                final var newCoord = new Coord(grid.size(), x, currentChar);
                if (currentChar == 'S') {
                    start = newCoord;
                } else if (currentChar == 'E') {
                    end = newCoord;
                }

                listX.add(newCoord);
            }

            grid.add(listX);
        }

        public Coord get(int row, int col) {
            return grid.get(row).get(col);
        }

        public int rowCount() {
            return grid.size();
        }

        public int colCount() {
            return grid.get(0).size();
        }

        public Coord findStart() {
            return start;
        }

        public Coord findEnd() {
            return end;
        }

        public void resetPos() {
            grid.stream().flatMap(Collection::stream).forEach(Coord::resetDistance);
        }

        public List<Coord> findACoords() {
            final var aCoords = new ArrayList<Coord>();
            for (List<Coord> row : grid) {
                for (Coord coord : row) {
                    if (coord.elevation == 'a') {
                        aCoords.add(coord);
                    }
                }
            }
            return aCoords;
        }
    }

    private static class Coord implements Comparable<Coord> {
        int row;
        int col;
        char elevation;
        int distance;

        public Coord(int row, int col, char c) {
            this.row = row;
            this.col = col;
            elevation = switch (c) {
                case 'S' -> 'a';
                case 'E' -> 'z';
                default -> c;
            };
        }

        @Override
        public int compareTo(Coord o) {
            return Integer.compare(distance, o.distance);
        }

        public void resetDistance() {
            distance = Integer.MAX_VALUE;
        }
    }
}
