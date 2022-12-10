import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class Day10 extends BaseDay {
    private final static String inputFile = "day10.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day10();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var instructions = readClassPathResource(inputFile).lines()
            .map(Instruction::parseInstruction)
            .toList();


        long signalStrength = 0;
        int cycle = 1;
        int register = 1;
        int instructionCount = 0;
        boolean addCycle = false;

        while (cycle <= 220) {

            if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
                signalStrength += ((long) cycle * register);
            }

            if (instructionCount <= instructions.size()) {
                if (instructions.get(instructionCount).addVal != null) {
                    if (addCycle) {
                        register += instructions.get(instructionCount).addVal;
                        addCycle = false;
                        instructionCount++;
                    } else {
                        addCycle = true;
                    }
                } else {
                    addCycle = false;
                    instructionCount++;
                }
            }



            cycle++;
        }


        log.info("Results: {}", signalStrength);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }

    public record Instruction(Integer addVal) {
        public static Instruction parseInstruction(String input) {
            Integer val = null;
            final var inputArr = input.split(" ");
            if (inputArr[0].equals("addx")) {
                val = Integer.parseInt(inputArr[1]);
            }
            return new Instruction(val);
        }
    }
}
