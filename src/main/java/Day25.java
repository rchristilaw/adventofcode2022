import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Slf4j
public class Day25 extends BaseDay {

    private static final List<Character> orderedValues = List.of('0', '1', '2', '=', '-');
    private static final Map<Character, Long> map = Map.of(
        '0', 0L,
        '1', 1L,
        '2', 2L,
        '=', -2L,
        '-', -1L);

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day25();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        long total = 0;

        for (var line : input.lines().toList()) {
            total += parseNumber(line);
        }

        log.info("Part 1 solution: {} = {}", total, translate(total));
        // -1326771281
        // SNAFU :  261


        log.info("Part 2 solution: {}", 0);
    }

    private long parseNumber(String val) {
        final var values = val.split("");
        final var digitSize = values.length;

        long fiveValue = 1;
        long decimal = 0;
        for (int i = 1; i <= values.length; i++) {
            final var current = map.get(values[digitSize - i].charAt(0));

            decimal += fiveValue * current;
            fiveValue = fiveValue * 5;
        }

        return decimal;
    }

    private String translate(Long decimal) {
        String snafu = "";
        while (decimal > 0) {
            snafu = orderedValues.get((int) (decimal % 5)) + snafu;
            decimal -= (((decimal + 2) % 5) - 2);
            decimal = decimal / 5;
        }
        return snafu;
    }

}
