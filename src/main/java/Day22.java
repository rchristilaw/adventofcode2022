import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Day22 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day22();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        final var grid = new Grid();
        final var coords = new ArrayList<Coord>();

        final var lines = input.lines().toList();
        int y = 0;
        for (var line : lines) {
            if (line.length() <1) {
                break;
            }

            for (int x = 0; x < line.length(); x++) {
                final var value = line.charAt(x);
                coords.add(new Coord(x, y, value == ' ' ? null : value, getCube(x, y)));
            }

            y++;
        }

        grid.initializeGrid(coords);

        final var moves = parseMoves(lines.get(y + 1));
        for (var move : moves) {
            grid.move(move);
        }

        final var row = (grid.currentY + 1) * 1000;
        final var col = (grid.currentX + 1) * 4;
        log.info("Part 1 solution: {}", row + col + grid.currentDirection);


        log.info("Part 2 solution: {}", 0);
    }

    private List<String> parseMoves(String movesLine) {
        final var moves = new ArrayList<String>();
        var val = "";
        for (var c : movesLine.split("")) {
            if (c.equals("R") || c.equals("L")) {
                if (val.length() > 0) {
                    moves.add(val);
                    val = "";
                }
                moves.add(c);
            } else {
                val += c;
            }
        }
        if (val.length() > 0) {
            moves.add(val);
        }

        return moves;
    }

    public static class Grid {
        List<Coord> coords;

        int currentDirection;
        int currentX;
        int currentY;

        public void initializeGrid(List<Coord> coords) {
            this.coords = coords;
            final var start = this.getMinYAtX(0);
            this.currentX = start.x;
            this.currentY = start.y;
        }

        private Coord getMinYAtX(int x) {
            return coords.stream()
                .filter(it -> it.x == x && it.value != null)
                .min(Comparator.comparing(it -> it.y))
                .orElseThrow();
        }

        private Coord getMinXAtY(int y) {
            return coords.stream()
                .filter(it -> it.y == y && it.value != null)
                .min(Comparator.comparing(it -> it.x))
                .orElseThrow();
        }

        private Coord getMaxYAtX(int x) {
            return coords.stream()
                .filter(it -> it.x == x && it.value != null)
                .max(Comparator.comparing(it -> it.y))
                .orElseThrow();
        }

        private Coord getMaxXAtY(int y) {
            return coords.stream()
                .filter(it -> it.y == y && it.value != null)
                .max(Comparator.comparing(it -> it.x))
                .orElseThrow();
        }

        public void move(String instruction) {
            if (instruction.equals("R")) {
                if (currentDirection == 3) {
                    currentDirection = 0;
                } else {
                    currentDirection++;
                }
            } else if (instruction.equals("L")) {
                if (currentDirection == 0) {
                    currentDirection = 3;
                } else {
                    currentDirection--;
                }
            } else {
                var value = Integer.parseInt(instruction);
                if (currentDirection == 0) {
                    while(value > 0) {
                        var nextCoord = getCoord(currentX + 1, currentY);
                        if (nextCoord == null) {
                            final var wrapCoord = getMinXAtY(currentY);
                            currentX = wrapCoord.x;
                        } else if (nextCoord.value == '#') {
                            value = 0;
                        } else {
                            currentX++;
                        }
                        value --;
                    }
                } else if (currentDirection == 2) {
                    while(value > 0) {
                        var nextCoord = getCoord(currentX - 1, currentY);
                        if (nextCoord == null) {
                            final var wrapCoord = getMaxXAtY(currentY);
                            currentX = wrapCoord.x;
                        } else if (nextCoord.value == '#') {
                            value = 0;
                        } else {
                            currentX--;
                        }
                        value --;
                    }
                } else if (currentDirection == 1) {
                    while(value > 0) {
                        var nextCoord = getCoord(currentX, currentY + 1);
                        if (nextCoord == null) {
                            final var wrapCoord = getMinYAtX(currentX);
                            currentY = wrapCoord.y;
                        } else if (nextCoord.value == '#') {
                            value = 0;
                        } else {
                            currentY++;
                        }
                        value --;
                    }
                } else if (currentDirection == 3) {
                    while(value > 0) {
                        var nextCoord = getCoord(currentX, currentY - 1);
                        if (nextCoord == null) {
                            final var wrapCoord = getMaxYAtX(currentX);
                            currentY = wrapCoord.y;
                        } else if (nextCoord.value == '#') {
                            value = 0;
                        } else {
                            currentY--;
                        }
                        value --;
                    }
                }

            }
        }

        private Coord getCoord(int x, int y) {
            return coords.stream()
                .filter(it -> it.y == y && it.x == x && it.value != null)
                .findFirst().orElse(null);
        }

    }

    public record Coord(int x, int y, Character value, Character cube) {
    }

    private Cube getCube(int x, int y) {
        return cubes.entrySet().stream()
            .filter(it -> x <= it.getValue().x2 && x >= it.getValue().x1
                && y <= it.getValue().y2 && y >= it.getValue().y1)
            .map(Map.Entry::getValue)
            .findFirst().orElseThrow();
    }

    public static Map<Character, Cube> cubes = Map.of(
        'A', new Cube('A', 50,99,0,49),
        'B', new Cube('B', 100,149,0,49),
        'C', new Cube('C', 50,99,50,99),
        'D', new Cube('D', 50,99,100,149),
        'E', new Cube('E', 0,49,100,149),
        'F', new Cube('F', 0,0,150,199));

    public void getNextCube(Grid grid) {
        final var cube = getCube(grid.currentX, grid.currentY);
        final var direction = grid.currentDirection;
        if (cube.name == 'A') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 0; //B
                    grid.currentX = cubes.get('B').x1;
                    grid.currentY = grid.currentY;
                }
                case 1 -> {
                    grid.currentDirection = 1; //C
                    grid.currentX = grid.currentX + 1;
                    grid.currentY = grid.currentY;;
                }
                case 2 -> {
                    grid.currentDirection = 0; //E
                    grid.currentX = cubes.get('E').x1;
                    grid.currentY = cubes.get('E').y1 + cubes.get('A').y2 - grid.currentY;
                }
                case 3 -> {
                    grid.currentDirection = 0; //F
                    grid.currentX = cubes.get('F').x1;
                    grid.currentY = cubes.get('F').y1 + cubes.get('A').x2 - grid.currentX;

                }
                default -> throw new RuntimeException();
            }
        } else if (cube.name == 'B') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 2; //D
                    grid.currentX = cubes.get('D').x2;

                }
                case 1 -> {
                    grid.currentDirection = 2; //C
                    grid.currentX = cubes.get('C').x2;
                }
                case 2 -> {
                    grid.currentDirection = 2; //A
                    grid.currentX = cubes.get('A').x2;
                }
                case 3 -> {
                    grid.currentDirection = 3; //F

                    grid.currentY = cubes.get('F').y2;
                }
                default -> throw new RuntimeException();
            }
        } else if (cube.name == 'C') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 3; //B

                    grid.currentY = cubes.get('B').y2;
                }
                case 1 -> {
                    grid.currentDirection = 1; //D
                    grid.currentX = cubes.get('D').x1;
                }
                case 2 -> {
                    grid.currentDirection = 1; //E
                    grid.currentX = cubes.get('E').x1;
                }
                case 3 -> {
                    grid.currentDirection = 3; //A

                    grid.currentY = cubes.get('A').y2;
                }
                default -> throw new RuntimeException();
            }
        } else if (cube.name == 'D') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 0; //B
                }
                case 1 -> {
                    grid.currentDirection = 1; //C
                    grid.currentX = cubes.get('C').x1;
                }
                case 2 -> {
                    grid.currentDirection = 0; //E
                }
                case 3 -> {
                    grid.currentDirection = 0; //F
                }
                default -> throw new RuntimeException();
            }
        } else if (cube.name == 'E') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 0; //E
                }
                case 1 -> {
                    grid.currentDirection = 1; //F
                    grid.currentX = cubes.get('F').x1;
                    grid.currentY = cubes.get('F').y1;
                }
                case 2 -> {
                    grid.currentDirection = 0; //A
                }
                case 3 -> {
                    grid.currentDirection = 0; //C
                }
                default -> throw new RuntimeException();
            }
        } else if (cube.name == 'F') {
            switch (direction) {
                case 0 -> {
                    grid.currentDirection = 3; //D

                    grid.currentY = cubes.get('D').y2;
                }
                case 1 -> {
                    grid.currentDirection = 1; //B
                    grid.currentX = cubes.get('B').x1;
                    grid.currentY = cubes.get('B').y1;
                }
                case 2 -> {
                    grid.currentDirection = 1; //A
                    grid.currentX = cubes.get('A').x1;
                    grid.currentY = cubes.get('A').y1;
                }
                case 3 -> {
                    grid.currentDirection = 3; //E

                    grid.currentY = cubes.get('E').y2;

                }
                default -> throw new RuntimeException();
            }
        }
    }
    public record Cube(Character name, int x1, int x2, int y1, int y2) {

    }
}
