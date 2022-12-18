import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day18 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day18();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {

        final var cubes = input.lines()
                .map(it -> it.split(","))
                .map(it -> new Cube(Integer.parseInt(it[0]), Integer.parseInt(it[1]), Integer.parseInt(it[2])))
                .toList();

        var totalFaces = cubes.size() * 6;
        for (var cube : cubes) {
            for (var otherCube : cubes) {
                if (cube.equals(otherCube)) {
                    continue;
                }
                if (cube.isNeighbour(otherCube)) {
                    totalFaces--;
                }
            }
        }

        log.info("Part 1 solution: {}", totalFaces);

        final var grid = new Grid();
        grid.lava = cubes;
        floodIt(grid, 0, 0, 0);

        log.info("Part 2 solution: {}", grid.externalFaces);
    }

    private void floodIt(Grid grid, int x, int y, int z) {

        final var stack = new LinkedList<Cube>();
        stack.push(new Cube(x, y, z));

        while (stack.size() > 0) {
            final var newCube = stack.pop();

            if (evaluate(grid, newCube)) {
                stack.push(new Cube(newCube.x - 1, newCube.y, newCube.z));
                stack.push(new Cube(newCube.x + 1, newCube.y, newCube.z));
                stack.push(new Cube(newCube.x, newCube.y - 1, newCube.z));
                stack.push(new Cube(newCube.x, newCube.y + 1, newCube.z));
                stack.push(new Cube(newCube.x, newCube.y, newCube.z - 1));
                stack.push(new Cube(newCube.x, newCube.y, newCube.z + 1));
            }
        }
    }

    private boolean evaluate(Grid grid, Cube cube) {
        if (cube.x < grid.getMinX() || cube.x > grid.getMaxX()
                || cube.y < grid.getMinY() || cube.y > grid.getMaxY()
                || cube.z < grid.getMinZ() || cube.z > grid.getMaxZ()) {
            return false;
        }

        if (grid.isVisited(cube)) {
            return false;
        }

        if (grid.isLava(cube)) {
            grid.externalFaces++;
            return false;
        }

        grid.visited.add(cube);
        return true;
    }

    public static class Grid {
        List<Cube> lava = new ArrayList<>();
        List<Cube> visited = new ArrayList<>();

        int externalFaces = 0;

        boolean isVisited(Cube cube) {
            return visited.stream()
                    .anyMatch(it -> it.equals(cube));
        }

        boolean isLava(Cube cube) {
            return lava.stream()
                    .anyMatch(it -> it.equals(cube));
        }

        int getMinX() {
            return lava.stream()
                    .map(it -> it.x)
                    .min(Comparator.naturalOrder()).orElse(0) - 10;
        }

        int getMinY() {
            return lava.stream()
                    .map(it -> it.y)
                    .min(Comparator.naturalOrder()).orElse(0) - 10;
        }

        int getMinZ() {
            return lava.stream()
                    .map(it -> it.z)
                    .min(Comparator.naturalOrder()).orElse(0) - 10;
        }

        int getMaxX() {
            return lava.stream()
                    .map(it -> it.x)
                    .max(Comparator.naturalOrder()).orElse(0) + 10;
        }

        int getMaxY() {
            return lava.stream()
                    .map(it -> it.y)
                    .max(Comparator.naturalOrder()).orElse(0) + 10;
        }

        int getMaxZ() {
            return lava.stream()
                    .map(it -> it.z)
                    .max(Comparator.naturalOrder()).orElse(0) + 10;
        }
    }

    public record Cube(int x, int y, int z) {

        boolean equals(Cube other) {
            return x == other.x && y == other.y && z == other.z;
        }

        boolean isNeighbour(Cube other) {
            if ((x - 1 == other.x && y == other.y && z == other.z)
                    || (x + 1 == other.x && y == other.y && z == other.z)
                    || (x == other.x && y - 1 == other.y && z == other.z)
                    || (x == other.x && y + 1 == other.y && z == other.z)
                    || (x == other.x && y == other.y && z - 1 == other.z)
                    || (x == other.x && y == other.y && z + 1 == other.z)) {
                return true;
            }
            return false;
        }
    }
}
