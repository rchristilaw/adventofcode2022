import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
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
        var instructions = input.lines()
            .map(Instruction::new)
            .toList();
//
//        final var rootValue = part1(new ArrayList<>(instructions));
//
//        log.info("Part 1 solution: {}", rootValue);

        final var humnValue = part2(new ArrayList<>(instructions));
        log.info("Part 2 solution: {}", humnValue);
    }

    private Long part1(List<Instruction> instructions) {
        final var valueMap = instructions.stream()
            .filter(it -> it.number != null)
            .collect(Collectors.toMap(it -> it.monkeyName, it -> (long)it.number));

        instructions.removeIf(it -> it.number != null);

        while (true) {
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
                        return result;
                    }
                }
            }
            instructions.removeIf(it -> it.number != null);

            if (valueMap.containsKey("root")) {
                return valueMap.get("root");
            }
        }
    }

    private Long part2(List<Instruction> instructionsInput) {
        final var humnInstruction = instructionsInput.stream()
            .filter(it -> it.monkeyName.equals("humn"))
            .findFirst().orElseThrow();
        instructionsInput.remove(humnInstruction);

        final var rootInstruction = instructionsInput.stream()
            .filter(it -> it.monkeyName.equals("root"))
            .findFirst().orElseThrow();
        instructionsInput.remove(rootInstruction);


        for (long humn = 0L; humn < Long.MAX_VALUE; humn++) {
            final var instructions = new ArrayList<>(instructionsInput);
            instructions.forEach(it -> {
                if (it.operation != null) {
                    it.number = null;
                }
            });

            final var valueMap = instructions.stream()
                .filter(it -> it.number != null)
                .collect(Collectors.toMap(it -> it.monkeyName, it -> (long)it.number));

            valueMap.put("humn", humn);

            instructions.removeIf(it -> it.number != null);

            while (instructions.size() > 0) {
                for (var instruction : instructions) {

                    if (instruction.number != null) {
                        valueMap.put(instruction.monkeyName, (long) instruction.number);
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
                        instruction.number = result;
                        valueMap.put(instruction.monkeyName, result);
                    }
                }
                instructions.removeIf(it -> it.number != null);
            }

            if (valueMap.containsKey(rootInstruction.operand1) && valueMap.containsKey(rootInstruction.operand2)) {
                final var op1 = valueMap.get(rootInstruction.operand1);
                final var op2 = valueMap.get(rootInstruction.operand2);

                if (Objects.equals(op1, op2)) {
                    return humn;
                }
            }
        }
        return null;
    }

    public static class Instruction {
        String monkeyName;
        Long number;

        Character operation;
        String operand1;
        String operand2;

        public Instruction(String line) {
            final var split = line.split(":");
            this.monkeyName = split[0];

            final var operationSplit = split[1].split(" ");
            if (operationSplit.length == 2) {
                this.number = Long.parseLong(operationSplit[1]);
            } else {
                this.operand1 = operationSplit[1];
                this.operand2 = operationSplit[3];
                this.operation = operationSplit[2].charAt(0);
            }

        }
    }
}
