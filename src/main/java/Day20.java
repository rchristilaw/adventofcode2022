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
        boolean part2 = true;
        final var decryptionKey = part2 ? 811589153L : 1;

        AtomicInteger index = new AtomicInteger();
        var lines = input.lines()
            .map(it -> new Line(index.getAndIncrement(), Long.parseLong(it) * decryptionKey))
            .collect(Collectors.toList());


        for (int round = 0; round < (part2 ? 10 : 1); round++) {
            for (int i = 0; i < lines.size(); i++) {

                int finalI = i;
                final var current = lines.stream()
                    .filter(it -> it.originalIndex == finalI)
                    .findFirst().orElseThrow();

                final long currentIndex = lines.indexOf(current);

                if (current.value == 0) {
                    continue;
                }

                long newIndex = currentIndex + current.value;
                if (newIndex < 0) {
                    newIndex = (newIndex % (lines.size() - 1)) + (lines.size() - 1);
                } else if (newIndex > lines.size() - 1) {
                    newIndex = newIndex % (lines.size() - 1);
                }

                if (newIndex == 0) {
                    newIndex = lines.size() - 1;
                }

                if (newIndex != currentIndex) {
                    lines.remove(current);
                    lines.add((int)newIndex, current);
                }

            }
            log.info("{}", lines.stream().map(it -> it.value).collect(Collectors.toList()));
//                .map(it -> new Line(x.getAndIncrement(), it.value))
//                .collect(Collectors.toList()););
//            AtomicInteger x = new AtomicInteger();
//            lines = lines.stream()
//                .map(it -> new Line(x.getAndIncrement(), it.value))
//                .collect(Collectors.toList());
        }

        final var zero = lines.stream()
            .filter(it -> it.value == 0)
            .findFirst().orElseThrow();
        final var zeroIndex = lines.indexOf(zero);

        long part1 = 0L;
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
        long value;
    }
}
