import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day23 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day23();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        final var grid = new Grid();
        final var lines = input.lines().toList();
        for (int y = 0; y < lines.size(); y++) {
            grid.elves.addAll(parseLine(lines.get(y), y));
        }

//        int round = 0;
//        while (round < 10) {
//            runRound(grid);
//            round++;
//        }
//
//        final var sizeX = (grid.getMaxX() - grid.getMinX()) + 1;
//        final var sizeY = (grid.getMaxY() - grid.getMinY()) + 1;
//
//        final var area = sizeY * sizeX;
//
//        log.info("Part 1 solution: {}", area - grid.elves.size());

        boolean elvesMoved = true;
        int round = 0;
        while (elvesMoved) {
            elvesMoved = runRound(grid) > 0;
            round++;
        }

        log.info("Part 2 solution: {}", round + 1);
    }

    private int runRound(Grid grid) {
        // SCOUT
        final var proposedMoves = new ArrayList<ProposedMove>();
        for (var elf : grid.elves) {
            Coord proposedCoord = null;
            if (grid.isOccupiedInAnyDirection(elf)) {
                for (var direction : grid.directions) {
                    if (!grid.isOccupiedInDirection(elf, direction)) {
                        proposedCoord = move(elf, direction);
                        break;
                    }
                }
            }

            proposedMoves.add(new ProposedMove(elf, proposedCoord));
        }

        // MOVE
        int movedElves = grid.evaluateProposedMoves(proposedMoves);
        grid.rotateDirections();
        return movedElves;
    }

    private List<Coord> parseLine(String line, int y) {
        final var lineArr = line.split("");
        final var elves = new ArrayList<Coord>();

        for (int x = 0; x < line.length(); x++) {
            if (lineArr[x].charAt(0) == '#') {
                elves.add(new Coord(x, y));
            }
        }
        return elves;
    }

    private Coord move(Coord coord, Direction direction) {
        if (direction == Direction.N) {
            return new Coord(coord.x, coord.y - 1);
        } else if (direction == Direction.S) {
            return new Coord(coord.x, coord.y + 1);
        } else if (direction == Direction.W) {
            return new Coord(coord.x - 1, coord.y);
        } else if (direction == Direction.E) {
            return new Coord(coord.x + 1, coord.y);
        }
        throw new RuntimeException("Invalid Direction");
    }


    public static class Grid {
        List<Coord> elves = new ArrayList<>();
        Queue<Direction> directions = new LinkedList<>();

        public Grid() {
            directions.add(Direction.N);
            directions.add(Direction.S);
            directions.add(Direction.W);
            directions.add(Direction.E);
        }

        public int getMinX() {
            return elves.stream()
                .map(it -> it.x)
                .min(Comparator.naturalOrder()).orElseThrow();
        }

        public int getMinY() {
            return elves.stream()
                .map(it -> it.y)
                .min(Comparator.naturalOrder()).orElseThrow();
        }

        public int getMaxX() {
            return elves.stream()
                .map(it -> it.x)
                .max(Comparator.naturalOrder()).orElseThrow();
        }

        public int getMaxY() {
            return elves.stream()
                .map(it -> it.y)
                .max(Comparator.naturalOrder()).orElseThrow();
        }

        public void rotateDirections() {
            final var first = directions.poll();
            directions.add(first);
        }

        public int evaluateProposedMoves(List<ProposedMove> proposedMoves) {
            List<Coord> newElves = new ArrayList<>();
            int elvesMoved = 0;
            for (var move : proposedMoves) {
                if (move.next == null) {
                    newElves.add(move.current);
                    continue;
                }

                final var count = proposedMoves.stream()
                    .filter(it -> it.next != null && it.next.x == move.next.x && it.next.y == move.next.y)
                    .count();

                if (count > 1) {
                    newElves.add(move.current);
                } else {
                    newElves.add(move.next);
                    elvesMoved++;
                }
            }
            this.elves = newElves;
            return elvesMoved;
        }

        private Coord getCoord(int x, int y) {
            return elves.stream()
                .filter(it -> it.x == x && it.y == y)
                .findFirst()
                .orElse(null);
        }

        public boolean isOccupiedInAnyDirection(Coord coord) {
            return isOccupiedInDirection(coord, Direction.N)
                || isOccupiedInDirection(coord, Direction.S)
                || isOccupiedInDirection(coord, Direction.W)
                || isOccupiedInDirection(coord, Direction.E);
        }

        public boolean isOccupiedInDirection(Coord coord, Direction direction) {
            if (direction == Direction.N) {
                return getCoord(coord.x - 1, coord.y - 1) != null
                    || getCoord(coord.x, coord.y - 1) != null
                    || getCoord(coord.x + 1, coord.y - 1) != null;
            } else if (direction == Direction.S) {
                return getCoord(coord.x - 1, coord.y + 1) != null
                    || getCoord(coord.x, coord.y + 1) != null
                    || getCoord(coord.x + 1, coord.y + 1) != null;
            } else if (direction == Direction.W) {
                return getCoord(coord.x - 1, coord.y + 1) != null
                    || getCoord(coord.x - 1, coord.y) != null
                    || getCoord(coord.x - 1, coord.y - 1) != null;
            } else if (direction == Direction.E) {
                return getCoord(coord.x + 1, coord.y + 1) != null
                    || getCoord(coord.x + 1, coord.y) != null
                    || getCoord(coord.x + 1, coord.y - 1) != null;
            }
            throw new RuntimeException("Invalid Direction");
        }
    }

    public record ProposedMove(Coord current, Coord next) {}
    public record Coord(int x, int y) {}

    public enum Direction {
        N, S, W, E;
    }
}
