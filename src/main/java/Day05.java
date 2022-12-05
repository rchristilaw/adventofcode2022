import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class Day05 extends BaseDay {
    private final static String inputFile = "day05.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day05();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var instructions = readClassPathResource(inputFile).lines()
            .filter(it -> it.startsWith("move"))
            .map(Instruction::parseString)
            .toList();

        final var stacks = buildStacks();

        for (var instruction : instructions) {
            for (int i = 0; i < instruction.count; i++) {
                final var crate = stacks.get(instruction.col1).pop();
                stacks.get(instruction.col2).push(crate);
            }
        }

        StringBuilder result = new StringBuilder();
        for (final var stack : stacks) {
            result.append(stack.pop());
        }

        log.info("Results: {}", result.toString());
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var instructions = readClassPathResource(inputFile).lines()
            .filter(it -> it.startsWith("move"))
            .map(Instruction::parseString)
            .toList();

        final var stacks = buildStacks();

        for (var instruction : instructions) {
            final var movedCrates = new ArrayList<Character>();
            for (int i = 0; i < instruction.count; i++) {
                final var crate = stacks.get(instruction.col1).pop();
                movedCrates.add(crate);
            }

            for (int i = instruction.count - 1; i >=0; i--) {
                stacks.get(instruction.col2).push(movedCrates.get(i));
            }
        }

        StringBuilder result = new StringBuilder();
        for (final var stack : stacks) {
            result.append(stack.pop());
        }

        log.info("Results: {}", result.toString());
    }

    private List<CrateStack> buildStacks() {
        final var crateStacks = new ArrayList<CrateStack>();
        crateStacks.add(CrateStack.parseString("QWPSZRHD"));
        crateStacks.add(CrateStack.parseString("VBRWQHF"));
        crateStacks.add(CrateStack.parseString("CVSH"));
        crateStacks.add(CrateStack.parseString("HFG"));
        crateStacks.add(CrateStack.parseString("PGJBZ"));
        crateStacks.add(CrateStack.parseString("QTJHWFL"));
        crateStacks.add(CrateStack.parseString("ZTWDLVJN"));
        crateStacks.add(CrateStack.parseString("DTZCJGHF"));
        crateStacks.add(CrateStack.parseString("WPVMBH"));
        return crateStacks;
    }

    public record Instruction(int count, int col1, int col2) {
        public static Instruction parseString(String instructions) {
            final var instructionsArray = instructions.split(" ");

            final var count = Integer.valueOf(instructionsArray[1]);
            final var col1 = Integer.parseInt(instructionsArray[3]) - 1;
            final var col2 = Integer.parseInt(instructionsArray[5]) - 1;
            return new Instruction(count, col1, col2);
        }
    }
    public record CrateStack(Stack<Character> crates) {
        public static CrateStack parseString(String letters) {
            final var stack = new Stack<Character>();
            for (var val : letters.split("")) {
                stack.push(val.charAt(0));
            }
            return new CrateStack(stack);
        }

        public Character pop() {
            return crates.pop();
        }

        public void push(Character c) {
            crates.push(c);
        }
    }
}
