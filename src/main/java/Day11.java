import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day11 extends BaseDay {
    private final static String inputFile = "day11.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day11();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
//        final var input = readClassPathResource(inputFile);
        final var monkeys = buildMonkeys();

        int round = 1;

        while (round <= 20) {
            for (final var monkey : monkeys) {
                while (monkey.items.size() > 0) {
                    final var item = monkey.items.remove();
                    final var result = monkey.executeAction(item.worryLevel) / 3;
                    final var divisible = monkey.evaluateResult(result);

                    if (divisible) {
                        monkeys.get(monkey.trueMonkey).items.add(new Item(result));
                    } else {
                        monkeys.get(monkey.falseMonkey).items.add(new Item(result));
                    }
                    monkey.itemCount++;
                }
            }
            round++;
        }

        final var sortedMonkeys = monkeys.stream()
            .sorted(Comparator.comparing(Monkey::getItemCount).reversed())
            .toList();

        log.info("Results: Top Monkeys: {}, {}", sortedMonkeys.get(0).getItemCount(), sortedMonkeys.get(1).getItemCount());
    }

    public void runPart2() throws URISyntaxException, IOException {

    }

    @Data
    @AllArgsConstructor
    public static class Item {
        int worryLevel;
    }

    @Builder
    @Data
    public static class Monkey {
        @Builder.Default
        Queue<Item> items = new LinkedList<>();
        Action action;
        int actionValue;
        int divisor;
        int trueMonkey;
        int falseMonkey;
        int itemCount;

        int executeAction(int input) {
            if (this.action == Action.ADD) {
                return input + actionValue;
            } else if (this.action == Action.MULTIPLY && actionValue == -1) {
                return input * input;
            } else {
                return input * actionValue;
            }
        }

        boolean evaluateResult(int input) {
            return input % divisor == 0;
        }
    }

    enum Action {
        ADD, MULTIPLY
    }

    private List<Monkey> buildMonkeys() {
        final var monkeys = new ArrayList<Monkey>();

        final var monkey0 = Monkey.builder()
            .action(Action.MULTIPLY)
            .actionValue(11)
            .divisor(7)
            .trueMonkey(6)
            .falseMonkey(7)
            .build();

        monkey0.items.add(new Item(66));
        monkey0.items.add(new Item(79));
        monkeys.add(monkey0);

        final var monkey1 = Monkey.builder()
            .action(Action.MULTIPLY)
            .actionValue(17)
            .divisor(13)
            .trueMonkey(5)
            .falseMonkey(2)
            .build();

        monkey1.items.add(new Item(84));
        monkey1.items.add(new Item(94));
        monkey1.items.add(new Item(94));
        monkey1.items.add(new Item(81));
        monkey1.items.add(new Item(98));
        monkey1.items.add(new Item(75));
        monkeys.add(monkey1);

        final var monkey2 = Monkey.builder()
            .action(Action.ADD)
            .actionValue(8)
            .divisor(5)
            .trueMonkey(4)
            .falseMonkey(5)
            .build();

        monkey2.items.add(new Item(85));
        monkey2.items.add(new Item(79));
        monkey2.items.add(new Item(59));
        monkey2.items.add(new Item(64));
        monkey2.items.add(new Item(79));
        monkey2.items.add(new Item(95));
        monkey2.items.add(new Item(67));
        monkeys.add(monkey2);

        final var monkey3 = Monkey.builder()
            .action(Action.ADD)
            .actionValue(3)
            .divisor(19)
            .trueMonkey(6)
            .falseMonkey(0)
            .build();

        monkey3.items.add(new Item(70));
        monkeys.add(monkey3);

        final var monkey4 = Monkey.builder()
            .action(Action.ADD)
            .actionValue(4)
            .divisor(2)
            .trueMonkey(0)
            .falseMonkey(3)
            .build();

        monkey4.items.add(new Item(57));
        monkey4.items.add(new Item(69));
        monkey4.items.add(new Item(78));
        monkey4.items.add(new Item(78));
        monkeys.add(monkey4);

        final var monkey5 = Monkey.builder()
            .action(Action.ADD)
            .actionValue(7)
            .divisor(11)
            .trueMonkey(3)
            .falseMonkey(4)
            .build();

        monkey5.items.add(new Item(65));
        monkey5.items.add(new Item(92));
        monkey5.items.add(new Item(60));
        monkey5.items.add(new Item(74));
        monkey5.items.add(new Item(72));

        monkeys.add(monkey5);

        final var monkey6 = Monkey.builder()
            .action(Action.MULTIPLY)
            .actionValue(-1)
            .divisor(17)
            .trueMonkey(1)
            .falseMonkey(7)
            .build();

        monkey6.items.add(new Item(77));
        monkey6.items.add(new Item(91));
        monkey6.items.add(new Item(91));

        monkeys.add(monkey6);

        final var monkey7 = Monkey.builder()
            .action(Action.ADD)
            .actionValue(6)
            .divisor(3)
            .trueMonkey(2)
            .falseMonkey(1)
            .build();

        monkey7.items.add(new Item(76));
        monkey7.items.add(new Item(58));
        monkey7.items.add(new Item(57));
        monkey7.items.add(new Item(55));
        monkey7.items.add(new Item(67));
        monkey7.items.add(new Item(77));
        monkey7.items.add(new Item(54));
        monkey7.items.add(new Item(99));

        monkeys.add(monkey7);

        return monkeys;
    }
}
