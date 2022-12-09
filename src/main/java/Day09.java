import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day09 extends BaseDay {
    private final static String inputFile = "day09.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day09();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var instructions = readClassPathResource(inputFile).lines()
            .map(Instruction::parseInstruction)
            .toList();

        final var grid = new Grid();
        for (final var instruction : instructions)  {
            updatePosition(instruction.direction, instruction.steps, grid, false);
        }


        int count = 0;
        for (final var set : grid.getHistory().entrySet()) {
            count += set.getValue().size();
        }

        log.info("Results: {}", count);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var instructions = readClassPathResource(inputFile).lines()
            .map(Instruction::parseInstruction)
            .toList();

        final var grid = new Grid();
        for (final var instruction : instructions)  {
            updatePosition(instruction.direction, instruction.steps, grid, true);
        }


        int count = 0;
        for (final var set : grid.getHistory().entrySet()) {
            count += set.getValue().size();
        }

        log.info("Results: {}", count);
    }

    private Coords getCoordsDelta(Coords head, Coords tail) {
        final var coordsDelta = new Coords();
        coordsDelta.x = head.x - tail.x;
        coordsDelta.y = head.y - tail.y;
        return coordsDelta;
    }

    public void updatePosition(Character direction, int steps, Grid grid, boolean part2) {
        for (int step = 0; step < steps; step++) {
            if (direction == 'L') {
                grid.head.x -= 1;
            } else if (direction == 'R') {
                grid.head.x += 1;
            } else if (direction == 'U') {
                grid.head.y += 1;
            } else if (direction == 'D') {
                grid.head.y -= 1;
            } else {
                throw new RuntimeException();
            }

            var head = grid.head;
            if (!part2) {
                updateTail(head, grid.tail);
                updateHistory(grid, grid.tail);
            } else {
                for (var tail : grid.tails) {
                    updateTail(head, tail);
                    head = tail;
                }
                updateHistory(grid, grid.tails.get(8));
            }
        }
    }

    private void updateHistory(Grid grid, Coords tail) {
        final var history = grid.history;
        history.computeIfAbsent(tail.x, k -> new HashSet<Integer>());
        history.get(tail.x).add(tail.y);
    }

    private void updateTail(Coords head, Coords tail) {
        final var coordDelta = getCoordsDelta(head, tail);
        if (coordDelta.x > 1 && coordDelta.y >= 1 || coordDelta.x >= 1 && coordDelta.y > 1) {
            tail.x++;
            tail.y++;
        } else if (coordDelta.x > 1 && coordDelta.y <= -1 || coordDelta.x >= 1 && coordDelta.y < -1) {
            tail.x++;
            tail.y--;
        } else if (coordDelta.x < -1 && coordDelta.y >= 1 || coordDelta.x <= -1 && coordDelta.y > 1) {
            tail.x--;
            tail.y++;
        } else if (coordDelta.x < -1 && coordDelta.y <= -1 || coordDelta.x <= -1 && coordDelta.y < -1) {
            tail.x--;
            tail.y--;
        } else if (coordDelta.x > 1) {
            tail.x++;
        } else if (coordDelta.y > 1) {
            tail.y++;
        } else if (coordDelta.x < -1) {
            tail.x--;
        } else if (coordDelta.y < -1) {
            tail.y--;
        }
    }

    @Data
    public static class Grid {
        private Coords head = new Coords();
        private Coords tail = new Coords();;

        private List<Coords> tails = new ArrayList<>();

        Map<Integer, Set<Integer>> history = new HashMap<>();

        public Grid() {
            for (int i = 0; i < 9; i++) {
                tails.add(new Coords());
            }
        }
    }

    @Data
    public static class Coords {
        private int x = 0;
        private int y = 0;
    }

    public record Instruction(Character direction, int steps){
        public static Instruction parseInstruction(String instruction) {
            final var arr = instruction.split(" ");
            return new Instruction(arr[0].charAt(0), Integer.parseInt(arr[1]));
        }
    }
}
