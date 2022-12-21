import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class Day21 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day21();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        var instructions = new java.util.ArrayList<>(input.lines()
            .map(Instruction::new)
            .toList());

        final var valueMap = instructions.stream()
            .filter(it -> it.number != null)
            .collect(Collectors.toMap(it -> it.monkeyName, it -> (long)it.number));

        instructions.removeIf(it -> it.number != null);

        Long rootValue = null;

        while (rootValue == null) {
            for (var instruction : instructions) {
                if (instruction.number != null) {
                    valueMap.put(instruction.monkeyName, (long)instruction.number);
                }

                if (valueMap.containsKey(instruction.operand1) && valueMap.containsKey(instruction.operand2)) {
                    final var op1 = valueMap.get(instruction.operand1);
                    final var op2 = valueMap.get(instruction.operand2);
                    long result = 0;
                    if (instruction.operation == '+') {
                        result = op1 + op2;
                    } else if (instruction.operation == '-') {
                        result = op1 - op2;
                    } else if (instruction.operation == '*') {
                        result = op1 * op2;
                    } else if (instruction.operation == '/') {
                        result = op1 / op2;
                    }

                    valueMap.put(instruction.monkeyName, result);

                    if (instruction.monkeyName.equals("root")) {
                        rootValue = result;
                        break;
                    }
                }
            }
            instructions.removeIf(it -> it.number != null);

            if (valueMap.containsKey("root")) {
                rootValue = valueMap.get("root");
            }
        }

        log.info("Part 1 solution: {}", rootValue);


        log.info("Part 2 solution: {}", 0);
    }

    public static class Instruction {
        String monkeyName;
        Integer number;

        Character operation;
        String operand1;
        String operand2;

        Integer operandSolved1;
        Integer operandSolved2;

        boolean yelled;

        public Instruction(String line) {
            final var split = line.split(":");
            this.monkeyName = split[0];

            final var operationSplit = split[1].split(" ");
            if (operationSplit.length == 2) {
                this.number = Integer.parseInt(operationSplit[1]);
            } else {
                this.operand1 = operationSplit[1];
                this.operand2 = operationSplit[3];
                this.operation = operationSplit[2].charAt(0);
            }

        }
    }
}
