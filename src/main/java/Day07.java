import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class Day07 extends BaseDay {
    private final static String inputFile = "day07.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day07();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().toList();

        var resultSize = 0L;

        final var currentDirectoryStack = new Stack<Directory>();

        for (var line : input) {
            final var lineArr = line.split(" ");

            if (lineArr[0].equals("$")) {
                final var command = lineArr[1];

                if (command.equals("cd")) {
                    final var destination = lineArr[2];
                    if (destination.equals("..")) {
                        final var dir = currentDirectoryStack.pop();
                        final var dirSize = dir.calculateSize();
                        dir.setDirSize(dirSize);
                        if (dirSize <= 100000) {
                            resultSize += dirSize;
                        }
                    } else {
                        if (currentDirectoryStack.isEmpty()) {
                            final var destDir = new Directory("/");
                            currentDirectoryStack.push(destDir);
                        } else {
                            final var dir = currentDirectoryStack.peek();
                            final var destDir = dir.getChildDir(destination);
                            currentDirectoryStack.push(destDir);
                        }
                    }
                } else if (command.equals("ls")) {
                    // Do nothing
                }
            } else {
                if (lineArr[0].equals("dir")) {
                    final var newDir = new Directory(lineArr[1]);
                    currentDirectoryStack.peek().contents.add(newDir);
                } else {
                    final var newFile = new File(lineArr[1], Long.parseLong(lineArr[0]));
                    currentDirectoryStack.peek().contents.add(newFile);
                }
            }
        }

        log.info("Results: {}", resultSize);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().toList();

        final var currentDirectoryStack = new Stack<Directory>();

        for (var line : input) {
            final var lineArr = line.split(" ");

            if (lineArr[0].equals("$")) {
                final var command = lineArr[1];

                if (command.equals("cd")) {
                    final var destination = lineArr[2];
                    if (destination.equals("..")) {
                        final var dir = currentDirectoryStack.pop();
                        final var dirSize = dir.calculateSize();
                        dir.setDirSize(dirSize);
                    } else {
                        if (currentDirectoryStack.isEmpty()) {
                            final var destDir = new Directory("/");
                            currentDirectoryStack.push(destDir);
                        } else {
                            final var dir = currentDirectoryStack.peek();
                            final var destDir = dir.getChildDir(destination);
                            currentDirectoryStack.push(destDir);
                        }
                    }
                } else if (command.equals("ls")) {
                    // Do nothing
                }
            } else {
                if (lineArr[0].equals("dir")) {
                    final var newDir = new Directory(lineArr[1]);
                    currentDirectoryStack.peek().contents.add(newDir);
                } else {
                    final var newFile = new File(lineArr[1], Long.parseLong(lineArr[0]));
                    currentDirectoryStack.peek().contents.add(newFile);
                }
            }
        }

        while (!currentDirectoryStack.peek().dirName.equals("/")) {
            final var dir = currentDirectoryStack.pop();
            final var dirSize = dir.calculateSize();
            dir.setDirSize(dirSize);
        }

        final var dir = currentDirectoryStack.pop();
        final var dirSize = dir.calculateSize();
        dir.setDirSize(dirSize);

        final var targetFileDeletion =  dirSize - 40000000;

        final var result = findDirToDelete(dir, targetFileDeletion, null);

        log.info("Results: {}", result.dirSize);
    }

    private Directory findDirToDelete(Directory currentDirectory, long targetFileDeletion, Directory idealDirectory) {
        if (currentDirectory.dirSize > targetFileDeletion
            && (idealDirectory == null || currentDirectory.dirSize < idealDirectory.dirSize)) {
            idealDirectory = currentDirectory;
        }
        for (var object : currentDirectory.contents) {
            if (object instanceof Directory dir) {
                idealDirectory = findDirToDelete(dir, targetFileDeletion, idealDirectory);
            }
        }

        return idealDirectory;
    }
    public record File(String fileName, long fileSize) {

    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Directory {
        private String dirName;
        private Long dirSize;
        private List<Object> contents = new ArrayList<>();

        public Directory(String dirName) {
            this.dirName = dirName;
        }

        long calculateSize() {
            long totalSize = 0;
            for (final var obj : contents) {
                if (obj instanceof Directory dir) {
                    totalSize += dir.dirSize;
                } else if (obj instanceof File file) {
                    totalSize += file.fileSize;
                }
            }

            return totalSize;
        }

        Directory getChildDir(String name) {
            return contents.stream()
                .filter(it -> {
                    if (it instanceof Directory dir) {
                        return dir.dirName.equals(name);
                    } else {
                        return false;
                    }
                })
                .map(it -> (Directory) it)
                .findFirst()
                .orElseThrow();
        }


    }
}
