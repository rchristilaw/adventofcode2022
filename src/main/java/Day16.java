import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
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

        final var path = new Path();
        final var history = new History();
        history.valves = valves;
        history.pathMaxPressure = new HashMap<String, Integer>();

        var maxPath = navigate(history, "AA", path);

        log.info("Part 1 solution: {}", maxPath.pressureSum);


        log.info("Part 2 solution: {}", 0);
    }

    public Path navigate(History history, String destination, Path path) {
        path.minute++;
        if (path.minute > 30) {
            return path;
        }

        path.pressureSum += path.getMaxPressurePerMinute();

        long countNonZeroValves = history.valves.values().stream()
            .filter(it -> it.flowRate > 0)
            .count();

        final var pathLength = path.path.length();

        if (path.openValves.size() >= countNonZeroValves
            || (pathLength >= 4
            && path.path.substring(pathLength - 4, pathLength - 2).equals(destination))
            || (pathLength >= 6
            && path.path.substring(pathLength - 6, pathLength - 4).equals(destination)
            && !path.path.substring(pathLength - 4, pathLength - 2).equals(destination))
            || (pathLength >= 8
            && path.path.substring(pathLength - 8, pathLength - 6).equals(destination)
            && !path.path.substring(pathLength - 6, pathLength - 4).equals(destination))
        ) {
//            if (path.openValves.size() < countNonZeroValves) {
//                return path;
//            }

            while (true) {
                path.minute++;
                if (path.minute > 30) {
                    return path;
                }

                path.pressureSum += path.getMaxPressurePerMinute();
            }
        }

        path.path += destination;

//        final var existingPath = history.pathMaxPressure.get(path.path);
//        if (existingPath != null) {
//            path.setMaxPressurePerMinute(existingPath);
//            navigate(history, destination, path);
//        }

//        long countNonZeroValves = history.valves.values().stream()
//            .filter(it -> it.flowRate > 0)
//            .count();
//
//        if (path.openValves.size() >= countNonZeroValves) {
//            history.pathMaxPressure.put(path.path, path.getMaxPressurePerMinute());
//            return navigate(history, destination, path);
//        }

        final var valve = history.valves.get(destination);

        Path openedPath = null;
        if (valve.flowRate != 0 && !path.alreadyOpened(valve.name)) {
            path.openValves.add(valve);
            openedPath = navigate(history, destination, new Path(path));
        }

        final Path maxPressureNot = valve.connectedValveNames.parallelStream()
            .map(it -> navigate(history, it, new Path(path)))
            .max(Comparator.comparing(Path::getPressureSum))
            .orElse(null);

        if (openedPath != null && openedPath.pressureSum > Objects.requireNonNull(maxPressureNot).pressureSum) {
            return openedPath;
        } else {
            return maxPressureNot;
        }
    }


    public static class History {
        Map<String, Valve> valves;
        Map<String, Integer> pathMaxPressure;
    }

    @NoArgsConstructor
    public class Path {
        List<Valve> openValves = new ArrayList<>();
        String path = "";
        int pressureSum = 0;

        Integer maxPressurePerMinute;
        int minute;

        public Path(Path other) {
            this.openValves = new ArrayList<>(other.openValves);
            this.path = other.path;
            this.pressureSum = other.pressureSum;
            this.maxPressurePerMinute = other.maxPressurePerMinute;
            this.minute = other.minute;
        }

        public int getPressureSum() {
            return pressureSum;
        }

        public void setMaxPressurePerMinute(Integer val) {
            maxPressurePerMinute = val;
        }

        public int getMaxPressurePerMinute() {
            return openValves.stream()
                .map(it -> it.flowRate)
                .reduce(0, Integer::sum);
        }

        public boolean alreadyOpened(String valveName) {
            return openValves.stream()
                .anyMatch(it -> it.getName().equals(valveName));
        }

    }

    public class Valve {
        String name;
        int flowRate;
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
