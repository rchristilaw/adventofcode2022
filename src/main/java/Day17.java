import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Day17 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day17();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        final var jetGas = Arrays.stream(input.split(""))
            .collect(Collectors.toCollection(LinkedList::new));

        final var cave = new Cave();
        int rockCount = 0;
        var currentRock = new Rock(0, cave.getMaxHeight() + 4);
        while (rockCount < 2022) {

            // Horizontal
            var movedRock = new Rock(currentRock);
            final var currentJetDirection = jetGas.pop();
            jetGas.add(currentJetDirection);
            movedRock.moveHorizontal(currentJetDirection);
            if (cave.canRockMove(movedRock)) {
                currentRock = movedRock;
            }

            // Down
            movedRock = new Rock(currentRock);
            movedRock.dropRock();
            if (cave.canRockMove(movedRock)) {
                currentRock = movedRock;
            } else  {
                cave.addRockToFloor(currentRock);
                rockCount++;
                currentRock = new Rock(rockCount, cave.getMaxHeight() + 4);
            }
        }

        log.info("Part 1 solution: {}", cave.getMaxHeight());


        log.info("Part 2 solution: {}", 0);
    }

    @NoArgsConstructor
    public static class Cave {
        List<Coord> rocks = new ArrayList<>();

        public int getMaxHeight() {
            return rocks.stream()
                .map(it -> it.y)
                .max(Comparator.naturalOrder()).orElse(-1);
        }

        public boolean canRockMove(Rock rock) {
            if (rock.getMaxX() > 6 || rock.getMinX() < 0) {
                return false;
            }

            for (var coord : rock.coords) {
                if (coord.y <= -1) {
                    return false;
                }
                for (var floorCoord : rocks) {
                    final var rockContact = floorCoord.x == coord.x && floorCoord.y == coord.y;
                    if (rockContact) {
                        return false;
                    }
                }

            }
            return true;
        }

        public void addRockToFloor(Rock rock) {
            rocks.addAll(rock.coords);
        }
    }

    public static class Rock {
        List<Coord> coords = new ArrayList<>();

        public Rock(Rock otherRock) {
            for (var coord : otherRock.coords) {
                this.coords.add(new Coord(coord.x, coord.y));
            }
        }

        public Rock(int rockNum, int initY) {
            init(rockNum % 5, initY);
        }

        private void init(int rockNum, int initY) {
            if (rockNum == 0) {
                coords.addAll(List.of(
                    new Coord(2, initY),
                    new Coord(3, initY),
                    new Coord(4, initY),
                    new Coord(5, initY)
                ));
            } else if (rockNum == 1) {
                coords.addAll(List.of(
                    new Coord(2, initY + 1),
                    new Coord(3, initY + 2),
                    new Coord(3, initY + 1),
                    new Coord(3, initY),
                    new Coord(4, initY + 1)
                ));
            } else if (rockNum == 2) {
                coords.addAll(List.of(
                    new Coord(2, initY),
                    new Coord(3, initY),
                    new Coord(4, initY),
                    new Coord(4, initY + 1),
                    new Coord(4, initY + 2)
                ));
            } else if (rockNum == 3) {
                coords.addAll(List.of(
                    new Coord(2, initY),
                    new Coord(2, initY + 1),
                    new Coord(2, initY + 2),
                    new Coord(2, initY + 3)
                ));
            } else if (rockNum == 4) {
                coords.addAll(List.of(
                    new Coord(2, initY),
                    new Coord(2, initY + 1),
                    new Coord(3, initY),
                    new Coord(3, initY + 1)
                ));
            } else {
                throw new RuntimeException("Invalid rock");
            }
        }

        private int getMaxX() {
            return coords.stream()
                .map(it -> it.x)
                .max(Comparator.naturalOrder()).orElse(0);
        }

        private int getMinX() {
            return coords.stream()
                .map(it -> it.x)
                .min(Comparator.naturalOrder()).orElse(0);
        }

        public Integer getMinYAtX(int x) {
            return coords.stream()
                .filter(it -> it.x == x)
                .map(it -> it.y)
                .min(Comparator.naturalOrder()).orElse(null);
        }

        public Integer getMaxYAtX(int x) {
            return coords.stream()
                .filter(it -> it.x == x)
                .map(it -> it.y)
                .max(Comparator.naturalOrder()).orElse(null);
        }


        public void dropRock() {
            for (var coord : coords) {
                coord.y = coord.y - 1;
            }
        }

        public void moveHorizontal(String direction) {
            final var right = direction.equals(">");
            for (var coord : coords) {
                final var val = right ? 1 : -1;
                coord.x = coord.x + val;
            }
        }
    }

    @AllArgsConstructor
    public static class Coord {
        int x;
        int y;
    }
}
