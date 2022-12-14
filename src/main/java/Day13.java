import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Day13 extends BaseDay {
    private final static String inputFile = "day13.txt";

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day13();
        day.runPart1();
        day.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().toList();

        final var pairs = new ArrayList<Pair>();

        var pair = new Pair();
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).length() > 0) {
                pair.setPacketA(new Packet(input.get(i)));
                i++;
                pair.setPacketB(new Packet(input.get(i)));
                pairs.add(pair);
            } else {
                pair = new Pair();
            }
        }

        long count = 0;
        long pairCount = 1;
        for (var p : pairs) {
            if (p.packetA.compare(p.packetB) <= 0) {
                count += pairCount;
            }
            pairCount++;
        }

        log.info("Results: {}", count);
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile).lines().toList();

        final var packets = new ArrayList<Packet>();

        for (var line : input) {
            if (line.length() > 0) {
                packets.add(new Packet(line));
            }
        }
        final var tracerPacket1 = new Packet("[[2]]");
        final var tracerPacket2 = new Packet("[[6]]");
        packets.add(tracerPacket1);
        packets.add(tracerPacket2);


        final var sortedPackets = packets.stream()
            .sorted(Packet::compare)
            .toList();

        long key = 1;
        for (int i = 0; i < sortedPackets.size(); i++) {
            if (tracerPacket1 == sortedPackets.get(i)
                || tracerPacket2 == sortedPackets.get(i)) {
                key = key * (i + 1);
            }
        }

        log.info("Results: {}", key);
    }

    @Data
    public static class Pair {
        Packet packetA;
        Packet packetB;

        public boolean isOrdered() {
            return true;
        }

    }

    public static class Packet {
        List<Packet> packets;
        Integer value;

        public Packet(String value) {
            this.packets = parse(value);
        }

        public int compare(Packet otherPacket) {
            if (this.isLeaf() && otherPacket.isLeaf()) {
                return this.value - otherPacket.value;
            } else if (!this.isLeaf() && !otherPacket.isLeaf()) {
                final var thisSize = this.packets.size();
                final var otherSize = otherPacket.packets.size();

                for (int i = 0; i < Math.min(thisSize, otherSize); i++) {
                    final int isSmaller = this.packets.get(i).compare(otherPacket.packets.get(i));
                    if (isSmaller != 0) {
                        return isSmaller;
                    }
                }

                return thisSize - otherSize;
            } else if (this.isLeaf()) {
                return new Packet(List.of(this)).compare(otherPacket);
            } else {
                return this.compare(new Packet(List.of(otherPacket)));
            }
        }

        boolean isLeaf() {
            return value != null;
        }

        public Packet(List<Packet> packets) {
            this.packets = packets;
        }

        public Packet(Integer value) {
            this.value = value;
        }
    }

    public static List<Packet> parse(String packetString) {
        final var queue = Arrays.stream(packetString.split(""))
            .map(it -> it.charAt(0))
            .collect(Collectors.toCollection(LinkedList::new));
        queue.poll(); //discard first [
        return parse(queue);
    }

    public static List<Packet> parse(Queue<Character> packet) {
        final var nodes = new ArrayList<Packet>();
        var val = packet.poll();
        if (val != null && val == ']') {
            return nodes;
        }

        while (val != null && val != ']') {
            if (val == '[') {
                nodes.add(new Packet(parse(packet)));
            } else if (val == ',') {
                //noop
            } else {
                var num = val.toString();
                while (packet.peek() != null
                    && packet.peek() != '['
                    && packet.peek() != ','
                    && packet.peek() != ']') {
                    num += packet.poll();
                }
                nodes.add(new Packet(Integer.parseInt(num)));
            }
            val = packet.poll();
        }
        return nodes;
    }
}
