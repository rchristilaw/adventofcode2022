import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Day20 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day20();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        AtomicInteger index = new AtomicInteger();
        var lines = input.lines()
            .map(it -> new Line(index.getAndIncrement(), Integer.parseInt(it)))
            .collect(Collectors.toList());


//        for (int round = 0; round < 1000; round++) {
            for (int i = 0; i < lines.size(); i++) {

                int finalI = i;
                final var current = lines.stream()
                    .filter(it -> it.originalIndex == finalI)
                    .findFirst().orElseThrow();

                final var currentIndex = lines.indexOf(current);
                var newIndex = currentIndex + current.value;
                if (newIndex < 0) {
                    while (newIndex < 0) {
                        newIndex = (lines.size() - 1) + newIndex;
                    }
                } else if (newIndex > lines.size() - 1) {
                    newIndex = newIndex % (lines.size() - 1);
                }

                if (newIndex == 0) {
                    newIndex = lines.size() - 1;
                }

                if (newIndex > currentIndex) {
                    lines.remove(current);
                    lines.add(newIndex, current);
                } else if (newIndex < currentIndex) {
                    lines.remove(current);
                    lines.add(newIndex, current);
                }

            }
            AtomicInteger x = new AtomicInteger();
            lines = lines.stream()
                .map(it -> new Line(x.getAndIncrement(), it.value))
                .collect(Collectors.toList());
//        }

        final var zero = lines.stream()
            .filter(it -> it.value == 0)
            .findFirst().orElseThrow();
        final var zeroIndex = lines.indexOf(zero);

        var part1 = 0;
        var next = zeroIndex + 1000;
        while (next > lines.size() - 1) {
            next = next % lines.size();
        }
        part1 += lines.get(next).value;

        next = next + 1000;
        while (next > lines.size() - 1) {
            next = next % lines.size();
        }
        part1 += lines.get(next).value;

        next = next + 1000;
        while (next > lines.size() - 1) {
            next = next % lines.size();
        }
        part1 += lines.get(next).value;

        log.info("Part 1 solution: {}", part1);


        log.info("Part 2 solution: {}", 0);
    }

    @AllArgsConstructor
    public static class Line {
        int originalIndex;
        int value;
    }
}
