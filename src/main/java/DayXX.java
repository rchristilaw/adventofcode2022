import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class DayXX extends BaseDay {
    private final static String inputFile = "dayXX.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var dayXX = new DayXX();
        dayXX.runPart1();
        dayXX.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }
}
