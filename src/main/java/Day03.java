import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day03 extends BaseDay {
    private final static String inputFile = "day03.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var dayXX = new Day03();
        dayXX.runPart1();
        dayXX.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var ruckSacks = readClassPathResource(inputFile).lines()
            .map(RuckSack::parseCompartments)
            .toList();

        int prioritySum = 0;
        for (var sack : ruckSacks) {
            final var matchingItem = findMatchingItem(sack);
            prioritySum += getPriority(matchingItem);
        }

        log.info("Results: {}", prioritySum);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var ruckSacks = readClassPathResource(inputFile).lines()
            .map(RuckSack::parseFullSack)
            .toList();

        int prioritySum = 0;
        for (int i = 0; i < ruckSacks.size(); i = i + 3) {
            final var matchingItem = findMatchingItemPart2(ruckSacks.get(i), ruckSacks.get(i + 1), ruckSacks.get(i + 2));
            prioritySum += getPriority(matchingItem);
        }

        log.info("Results: {}", prioritySum);
    }

    private Character findMatchingItem(RuckSack ruckSack) {
        final var compartment1 = ruckSack.compartment1;
        final var compartment2 = ruckSack.compartment2;

        for (char c : compartment1) {
            for (var item : compartment2) {
                if (c == item) {
                    return item;
                }
            }
        }
        return null;
    }

    private Character findMatchingItemPart2(RuckSack ruckSack1, RuckSack ruckSack2, RuckSack ruckSack3) {
        for (char c : ruckSack1.fullRuckSack) {
            for (var item : ruckSack2.fullRuckSack) {
                if (c == item) {
                    for (var item3 : ruckSack3.fullRuckSack) {
                        if (item3 == c) {
                            return item3;
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Couldn't find matching item");
    }

    private int getPriority(Character value) {
        final var asciiVal = (int) value;

        if (asciiVal > 96) {
            return asciiVal - 96;
        } else {
            return asciiVal - 38;
        }
    }

    record RuckSack(List<Character> fullRuckSack, List<Character> compartment1, List<Character> compartment2) {
        static RuckSack parseCompartments(String fullRuckSack) {
            final var length = fullRuckSack.length();
            final var compartmentSize = length/2;

            final var ruckSackItems = fullRuckSack.split("");

            final var compartment1 = new ArrayList<Character>();
            final var compartment2 = new ArrayList<Character>();

            for (int i = 0; i < length; i++) {
                if (i < compartmentSize) {
                    compartment1.add(ruckSackItems[i].charAt(0));
                } else {
                    compartment2.add(ruckSackItems[i].charAt(0));
                }
            }



            return new RuckSack(null, compartment1, compartment2);
        }

        static RuckSack parseFullSack(String fullRuckSack) {
            final var fullSack = Arrays.stream(fullRuckSack.split(""))
                .map(it -> it.charAt(0))
                .toList();

            return new RuckSack(fullSack, null, null);
        }
    }
}
