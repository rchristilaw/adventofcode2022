import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
public class Day01 extends BaseDay {
    private final static String inputFile = "day01.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day1 = new Day01();
        day1.runPart1();
        day1.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var calorieReadings = input.lines()
            .map(it -> {
                try {
                    return Integer.valueOf(it);
                } catch (Exception exception) {
                    return null;
                }
            })
            .toList();

        final var sums = new ArrayList<Integer>();

        var currentSum = 0;
        for (final var reading : calorieReadings) {
            if (reading == null) {
                sums.add(currentSum);
                currentSum = 0;
                continue;
            }
            currentSum += reading;
        }

        final var orderedSums = sums.stream()
            .sorted()
            .toList();

        log.info("Results: {}", orderedSums);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);
        final var calorieReadings = input.lines()
            .map(it -> {
                try {
                    return Integer.valueOf(it);
                } catch (Exception exception) {
                    return null;
                }
            })
            .toList();

        final var sums = new ArrayList<Integer>();

        var currentSum = 0;
        for (final var reading : calorieReadings) {
            if (reading == null) {
                sums.add(currentSum);
                currentSum = 0;
                continue;
            }
            currentSum += reading;
        }

        final var orderedMonkeys = sums.stream()
            .sorted()
            .toList();

        final var numberOfMonkeys = orderedMonkeys.size();
        final var topMonkeys = orderedMonkeys.get(numberOfMonkeys - 1)
            + orderedMonkeys.get(numberOfMonkeys - 2)
            + orderedMonkeys.get(numberOfMonkeys - 3);

        log.info("Results: {}", topMonkeys);
    }
}
