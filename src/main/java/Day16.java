import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class Day16 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day16();

        final var input = getInput(day.getClass(), true);
        day.run(input);
    }

    public void run(String input) {
        final var valves = input.lines()
            .map(Valve::new)
            .collect(Collectors.toMap(Valve::getName, Function.identity()));

        int minute = 0;

        int maxPressure = navigate(valves, minute, "AA", 0, 0);



        log.info("Part 1 solution: {}", maxPressure);


        log.info("Part 2 solution: {}", 0);
    }

    public int navigate(Map<String, Valve> valves, int minute, String destination, int pressurePerMinute, int pressureSum) {
        minute++;
        if (minute > 30) {
            return pressureSum;
        }

        pressureSum += pressurePerMinute;

        final var valve = valves.get(destination);
        if (valve.flowRate > 0 && !valve.open) {
            valve.open = true;
            pressurePerMinute += valve.flowRate;
            minute++;
        }

        int maxPressure = 0;
        for (var option : valve.connectedValveNames) {
            final var currPressure = navigate(valves, minute, option, pressurePerMinute, pressureSum);
            if (currPressure > maxPressure) {
                maxPressure = currPressure;
            }
        }
        return maxPressure;
    }

    public class Valve {
        String name;
        int flowRate;
        boolean open;
        List<String> connectedValveNames = new ArrayList<>();

        public Valve(String input) {
            final var split = input.split(" ");
            name = split[1];
            flowRate = extractNumber(split[4]);

            for (int i = 9; i < split.length; i++) {
                connectedValveNames.add(split[i].replace(",", ""));
            }
        }

        public String getName() {
            return name;
        }
    }

    private int extractNumber(String string) {
        final var split = string.split("=");
        final var sanitizedString = split[1]
            .replaceAll(",", "").replaceAll(";", "");
        return Integer.parseInt(sanitizedString);
    }
}
