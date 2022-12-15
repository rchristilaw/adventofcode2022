import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class DayXX extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new DayXX();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {

        log.info("Part 1 solution: {}", 0);


        log.info("Part 2 solution: {}", 0);
    }
}
