import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Day08 extends BaseDay {
    private final static String inputFile = "day08.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day08();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var treeGrid = readClassPathResource(inputFile).lines()
            .map(it -> Arrays.stream(it.split(""))
                    .map(itt -> new Tree(Integer.parseInt(itt)))
                    .toList())
            .toList();

        checkVisibilityFromLeft(treeGrid);
        checkVisibilityFromRight(treeGrid);
        checkVisibilityFromTop(treeGrid);
        checkVisibilityFromBottom(treeGrid);

        int count = 0;
        int maxDistance = 0;
        for (int i = 0; i < treeGrid.size(); i ++) {
            for (int j = 0; j < treeGrid.get(0).size(); j++) {
                final var tree = treeGrid.get(i).get(j);
                if (tree.isVisible()) {
                    count++;
                }

                final var treeDistance = tree.getMaxDistance();
                if (treeDistance > maxDistance) {
                    maxDistance = treeDistance;
                }
            }
        }


        log.info("Results. Pt 1: {}, Max Val Pt 2: {}", count, maxDistance);
    }


    private void checkVisibilityFromLeft(List<List<Tree>> treeGrid) {
        for (int i = 0; i < treeGrid.size(); i ++) {
            int maxHeight = 0;
            List<Integer> previousHeights = new ArrayList<>();
            for (int j = 0; j < treeGrid.get(0).size(); j++) {

                final var currTree = treeGrid.get(i).get(j);
                if (j == 0) {
                    currTree.visibleLeft = true;
                    maxHeight = currTree.height;
                } else if (currTree.height > maxHeight) {
                    currTree.visibleLeft = true;
                    maxHeight = currTree.height;
                } else {
                    currTree.visibleLeft = false;
                }

                calculateDistance("L", previousHeights, currTree);
                previousHeights.add(currTree.height);
            }
        }
    }

    private void checkVisibilityFromRight(List<List<Tree>> treeGrid) {
        for (int i = 0; i < treeGrid.size(); i ++) {
            int maxHeight = 0;
            List<Integer> previousHeights = new ArrayList<>();
            for (int j = treeGrid.get(0).size() - 1; j >= 0; j--) {

                final var currTree = treeGrid.get(i).get(j);
                if (j == treeGrid.get(0).size() - 1) {
                    currTree.visibleRight = true;
                    maxHeight = currTree.height;
                } else if (currTree.height > maxHeight) {
                    currTree.visibleRight = true;
                    maxHeight = currTree.height;
                } else {
                    currTree.visibleRight = false;
                }

                calculateDistance("R", previousHeights, currTree);
                previousHeights.add(currTree.height);
            }
        }
    }

    private void checkVisibilityFromTop(List<List<Tree>> treeGrid) {
        for (int j = 0; j < treeGrid.get(0).size(); j++) {
            int maxHeight = 0;
            List<Integer> previousHeights = new ArrayList<>();
            for (int i = 0; i < treeGrid.size(); i ++) {
                final var currTree = treeGrid.get(i).get(j);
                if (i == 0) {
                    currTree.visibleTop = true;
                    maxHeight = currTree.height;
                } else if (currTree.height > maxHeight) {
                    currTree.visibleTop = true;
                    maxHeight = currTree.height;
                } else {
                    currTree.visibleTop = false;
                }

                calculateDistance("T", previousHeights, currTree);
                previousHeights.add(currTree.height);
            }
        }
    }

    private void checkVisibilityFromBottom(List<List<Tree>> treeGrid) {
        for (int j = 0; j < treeGrid.get(0).size(); j++) {
            int maxHeight = 0;
            List<Integer> previousHeights = new ArrayList<>();
            for (int i = treeGrid.size() - 1; i >= 0 ; i --) {
                final var currTree = treeGrid.get(i).get(j);

                if (i == treeGrid.get(0).size() - 1) {
                    currTree.visibleBottom = true;
                    maxHeight = currTree.height;
                } else if (currTree.height > maxHeight) {
                    currTree.visibleBottom = true;
                    maxHeight = currTree.height;
                } else {
                    currTree.visibleBottom = false;
                }

                calculateDistance("B", previousHeights, currTree);
                previousHeights.add(currTree.height);
            }
        }
    }

    private void calculateDistance(String direction, List<Integer> previousHeights, Tree currentTree) {
        if (previousHeights.isEmpty()) {
            currentTree.setDistance(direction, 0);
            return;
        }

        final var reverseHeights = new ArrayList<>(previousHeights);
        Collections.reverse(reverseHeights);

        int distance = 1;
        final var currentHeight = currentTree.height;

        for (int i = 0; i < reverseHeights.size() - 1; i++) {
            final var prevTree = reverseHeights.get(i);
            if (prevTree >= currentHeight) {
                break;
            }
            distance++;
        }

        currentTree.setDistance(direction, distance);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        log.info("Results: {}", 0);
    }

    @Data
    @NoArgsConstructor
    public static class Tree {
        private int height;
        private boolean visibleLeft;
        private boolean visibleRight;
        private boolean visibleTop;
        private boolean visibleBottom;

        private int viewLeft;
        private int viewRight;
        private int viewTop;
        private int viewBottom;

        public Tree(int height) {
            this.height = height;
        }

        public boolean isVisible() {
            return visibleLeft || visibleRight || visibleBottom || visibleTop;
        }

        public int getMaxDistance() {
            return viewLeft * viewRight * viewTop * viewBottom;
        }

        public void setDistance(String direction, int distance) {
            switch (direction) {
                case "L" -> this.viewLeft = distance;
                case "R" -> this.viewRight = distance;
                case "T" -> this.viewTop = distance;
                case "B" -> this.viewBottom = distance;
            }
        }
    }
}
