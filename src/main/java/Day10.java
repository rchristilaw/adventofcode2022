import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
        final var instructions = readClassPathResource(inputFile).lines()
            .map(Instruction::parseInstruction)
            .toList();


        long signalStrength = 0;
        int cycle = 0;
        int pixelPos = 0;
        int spritePos = 1;
        int instructionCount = 0;
        boolean addCycle = false;

        final var crt = new ArrayList<List<Character>>();
        var currentRow = new ArrayList<Character>();

        while (cycle <= 240) {
            if (instructionCount > instructions.size() - 1) {
                crt.add(currentRow);
                break;
            }

            if (cycle == 40 || cycle == 80 || cycle == 120 || cycle == 160 || cycle == 200 || cycle == 240) {
                crt.add(currentRow);
                currentRow = new ArrayList<Character>();
                pixelPos = 0;
            }

            if (pixelPos == spritePos || pixelPos == spritePos - 1 || pixelPos == spritePos + 1) {
                currentRow.add('#');
            } else {
                currentRow.add('.');
            }

            if (instructions.get(instructionCount).addVal != null) {
                if (addCycle) {
                    spritePos += instructions.get(instructionCount).addVal;
                    addCycle = false;
                    instructionCount++;
                } else {
                    addCycle = true;
                }
            } else {
                addCycle = false;
                instructionCount++;
            }

            pixelPos++;
            cycle++;
        }

        var printRow = "";
        printRow += '\n';
        for (final var row : crt) {

            for (final var col : row) {
                printRow += col.toString();
            }
            printRow += '\n';
        }
        log.info(printRow);
        log.info("Results: {}", signalStrength);
        //EHPZPJGL
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
