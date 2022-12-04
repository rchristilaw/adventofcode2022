import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class Day04 extends BaseDay {
    private final static String inputFile = "day04.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day04();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var pairs = readClassPathResource(inputFile).lines()
            .map(Pair::parseLine)
            .toList();

        int overlappingCount = 0;
        for (final var pair : pairs) {
            if (pair.isFullyOverlapping()) {
                overlappingCount++;
            }
        }

        log.info("Results: {}", overlappingCount);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var pairs = readClassPathResource(inputFile).lines()
            .map(Pair::parseLine)
            .toList();

        int overlappingCount = 0;
        for (final var pair : pairs) {
            if (pair.isOverlapping()) {
                overlappingCount++;
            }
        }

        log.info("Results: {}", overlappingCount);
    }

    public record Pair(int elf1Start, int elf1End, int elf2Start, int elf2End) {
        public static Pair parseLine(String line) {
            final var pair = line.split(",");
            final var elf1 = pair[0].split("-");
            final var elf2 = pair[1].split("-");

            return new Pair(
                Integer.parseInt(elf1[0]),
                Integer.parseInt(elf1[1]),
                Integer.parseInt(elf2[0]),
                Integer.parseInt(elf2[1]));
        }

        public boolean isFullyOverlapping() {
            if (elf1Start <= elf2Start && elf1End >= elf2End) {
                return true;
            }

            if (elf2Start <= elf1Start && elf2End >= elf1End) {
                return true;
            }

            return false;
        }

        public boolean isOverlapping() {
            if (elf2Start >= elf1Start && elf2Start <= elf1End) {
                return true;
            }

            if (elf1Start >= elf2Start && elf1Start <= elf2End) {
                return true;
            }

            if (elf1End >= elf2Start && elf1End <= elf2End) {
                return true;
            }

            if (elf2End >= elf1Start && elf2End <= elf1End) {
                return true;
            }

            return false;
        }
    }
}
