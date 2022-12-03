import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
public class Day02 extends BaseDay {
    private final static String inputFile = "day02.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day02 = new Day02();
        day02.runPart1();
        day02.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var rounds = readClassPathResource(inputFile).lines()
            .map(Round::parseRound)
            .toList();

        final var themMovesLegend = Map.of(
            "A", 1,
            "B", 2,
            "C", 3
        );

        final var meMovesLegend = Map.of(
            "X", 1,
            "Y", 2,
            "Z", 3
        );
        // A = Rock 1
        // B = Paper 2
        // C = Scissors 3

        long gameTotal = 0;
        for(final var round : rounds) {
            gameTotal += evaluateRoundPart1(themMovesLegend.get(round.them), meMovesLegend.get(round.me));
        }

        log.info("Results: {}", gameTotal);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var rounds = readClassPathResource(inputFile).lines()
            .map(Round::parseRound)
            .toList();

        final var themMovesLegend = Map.of(
            "A", 1,
            "B", 2,
            "C", 3
        );

        final var meMovesLegend = Map.of(
            "X", 1,
            "Y", 2,
            "Z", 3
        );
        // A = Rock 1
        // B = Paper 2
        // C = Scissors 3

        long gameTotal = 0;
        for(final var round : rounds) {
            gameTotal += evaluateRoundPart2(themMovesLegend.get(round.them), meMovesLegend.get(round.me));
        }

        log.info("Results: {}", gameTotal);
    }

    private int evaluateRoundPart1(int them, int me) {
        if (them == me) {
            return me + 3;
        } else if (me == 1 && them == 3
            || me == 2 && them == 1
            || me == 3 && them == 2) {
            return me + 6;
        } else {
            return me;
        }
    }

    private int evaluateRoundPart2(int them, int result) {
        if (result == 1) {
            return findLosingMove(them);
        } else if (result == 2) {
            return them + 3;
        } else {
            return findWinningMove(them) + 6;
        }
    }

    private int findWinningMove(int theirMove) {
        if (theirMove == 1) {
            return 2;
        } else if (theirMove == 2) {
            return 3;
        }  else {
            return 1;
        }
    }

    private int findLosingMove(int theirMove) {
        if (theirMove == 2) {
            return 1;
        } else if (theirMove == 3) {
            return 2;
        }  else {
            return 3;
        }
    }
    private record Round(String them, String me) {
        public static Round parseRound(String round) {
            final var moves = round.split(" ");
            return new Round(moves[0], moves[1]);
        }
    }
}
