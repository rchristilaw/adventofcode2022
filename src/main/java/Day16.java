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
        history.cachedPressure = new HashMap<>();

        var max = navigate(history, "AA", path);

        log.info("Part 1 solution: {}", max);


        log.info("Part 2 solution: {}", 0);
    }

    public int navigate(History history, String destination, Path path) {
        path.minute++;
        if (path.minute > 30) {
            return path.pressureSum;
        }

        final var cacheVal = CacheVal.buildCacheVal(path.minute, destination, path.openValves);
        if (history.cachedPressure.containsKey(cacheVal)) {
            return history.cachedPressure.get(cacheVal);
        }

        final var valve = history.valves.get(destination);

        int pressureOpened = 0;
        if (valve.flowRate != 0 && !path.alreadyOpened(valve.name)) {
            final var newPath = new Path(path);
            newPath.openValves.add(valve);
            final var remainingMinutes = 30 - path.minute;
            pressureOpened = remainingMinutes * valve.flowRate + navigate(history, destination, newPath);
        }

        int pressureNotOpened = 0;
        for (var nextValve : valve.connectedValveNames) {
            final int currentPressure = navigate(history, nextValve, new Path(path));
            pressureNotOpened = Math.max(pressureNotOpened, currentPressure);
        }

        int max = Math.max(pressureOpened, pressureNotOpened);
        history.cachedPressure.put(cacheVal, max);

        return max;
    }


    public static class History {
        Map<String, Valve> valves;
        Map<CacheVal, Integer> cachedPressure;
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

    public record CacheVal(int minute, String destination, List<String> openedValves) {

        public static CacheVal buildCacheVal(int minute, String destination, List<Valve> openValveObjs) {
            return new CacheVal(minute, destination, openValveObjs.stream().map(it -> it.name).sorted().toList());
        }


//        @Override
//        public boolean equals(Object obj) {
//            if (obj instanceof CacheVal o) {
//                if (this.minute == o.minute && this.destination.equals(o.destination)
//                    && this.openedValves.size() == o.openedValves.size()) {
//                    for (int i = 0; i < this.openedValves.size(); i++) {
//                        if (!this.openedValves.get(i).equals(o.openedValves.get(i))) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//            }
//            return false;
//        }
    }

    private int extractNumber(String string) {
        final var split = string.split("=");
        final var sanitizedString = split[1]
            .replaceAll(",", "").replaceAll(";", "");
        return Integer.parseInt(sanitizedString);
    }
}
