import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    private Character getCube(int x, int y) {
        if (y < 50 && x < 100) {
            return 'A';
        } else if (y < 50 && x >= 100) {
            return 'B';
        } else if (y >= 50 && y < 100) {
            return 'C';
        } else if (x >= 50 && y >= 100) {
            return 'D';
        } else if (x < 50 && y >= 100) {
            return 'E';
        } else if (y >= 150) {
            return 'F';
        }
        throw new RuntimeException("Wrong cube");
    }
}
