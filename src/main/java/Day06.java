import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
public class Day06 extends BaseDay {
    private final static String inputFile = "day06.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day06();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = Arrays.stream(readClassPathResource(inputFile).split(""))
            .map(it -> it.charAt(0))
            .toList();

        final var vals = new LinkedList<Character>();

        var markerIndex = 0;

        for (int i = 0; i < input.size(); i++) {
            final var c = input.get(i);

            if (vals.size() == 0) {
                vals.add(c);
                continue;
            }

            if (!vals.contains(c)) {
                vals.add(c);
            } else {
                while(vals.contains(c)) {
                    vals.remove(0);
                }
                vals.add(c);
            }

            if (vals.size() >= 4) {
                markerIndex = i + 1;
                break;
            }

        }

        log.info("Results: {}", markerIndex);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = Arrays.stream(readClassPathResource(inputFile).split(""))
            .map(it -> it.charAt(0))
            .toList();

        final var vals = new LinkedList<Character>();

        var markerIndex = 0;

        for (int i = 0; i < input.size(); i++) {
            final var c = input.get(i);

            if (vals.size() == 0) {
                vals.add(c);
                continue;
            }

            if (!vals.contains(c)) {
                vals.add(c);
            } else {
                while(vals.contains(c)) {
                    vals.remove(0);
                }
                vals.add(c);
            }

            if (vals.size() >= 14) {
                markerIndex = i + 1;
                break;
            }

        }

        log.info("Results: {}", markerIndex);
    }
}
