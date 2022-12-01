import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

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

        log.info("Results");
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results");
    }
}
