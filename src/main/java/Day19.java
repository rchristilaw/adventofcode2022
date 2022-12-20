import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Day19 extends BaseDay {

    public static void main(String... args) throws URISyntaxException, IOException {
        final var day = new Day19();

        final var input = getInput(day.getClass(), false);
        day.run(input);
    }

    public void run(String input) {
        final var blueprints = input.lines()
            .map(Blueprint::new)
            .toList();

        int part1Sum = 0;
        for (int i = 0; i < blueprints.size(); i++) {

            final var path = new Path();
            final var blueprint = blueprints.get(i);

            int max = executeBlueprint(blueprint, path, 24);

            log.info("Blueprint: {}: max {}", i + 1, max);

            part1Sum += ((i + 1) * max);
        }

        log.info("Part 1 solution: {}", part1Sum);


        log.info("Part 2 solution: {}", 0);
    }

    public int executeBlueprint(Blueprint blueprint, Path path, int maxMinute) {
        path.minute++;
        if (path.minute > maxMinute) {
            return path.geode;
        }

        int max = 0;

        if (blueprint.canAffordOre(path)) {
            final var newPath = new Path(path);
            newPath.subtractElements(blueprint.oreRobot);
            newPath.collect();
            newPath.oreRobots++;
            max = Math.max(max, executeBlueprint(blueprint, newPath, maxMinute));
        }

        if (blueprint.canAffordClay(path)) {
            final var newPath = new Path(path);
            newPath.subtractElements(blueprint.clayRobot);
            newPath.collect();
            newPath.clayRobots++;
            max = Math.max(max, executeBlueprint(blueprint, newPath, maxMinute));
        }

        if (blueprint.canAffordObsidian(path)) {
            final var newPath = new Path(path);
            newPath.subtractElements(blueprint.obsidianRobot);
            newPath.collect();
            newPath.obsidianRobots++;
            max = Math.max(max, executeBlueprint(blueprint, newPath, maxMinute));
        }

        if (blueprint.canAffordGeode(path)) {
            final var newPath = new Path(path);
            newPath.subtractElements(blueprint.geodeRobot);
            newPath.collect();
            newPath.geodeRobots++;
            max = Math.max(max, executeBlueprint(blueprint, newPath, maxMinute));
        }

        final var newPath = new Path(path);
        newPath.collect();
        max = Math.max(max, executeBlueprint(blueprint, newPath, maxMinute));

        return max;
    }

    @NoArgsConstructor
    public static class Path {
        int ore;
        int clay;
        int obsidian;
        int geode;

        int oreRobots = 1;
        int clayRobots;
        int obsidianRobots;
        int geodeRobots;

        int minute;

        public Path(Path other) {
            ore = other.ore;
            clay = other.clay;
            obsidian = other.obsidian;
            geode = other.geode;

            oreRobots = other.oreRobots;
            clayRobots = other.clayRobots;
            obsidianRobots = other.obsidianRobots;
            geodeRobots = other.geodeRobots;

            minute = other.minute;
        }

        public void collect() {
            ore += oreRobots;
            clay += clayRobots;
            obsidian += obsidianRobots;
            geode += geodeRobots;
        }

        public void subtractElements(List<Cost> costs) {

            for (var cost : costs) {
                if (cost.resource == Resource.ORE) {
                    ore = ore - cost.cost;
                }
                if (cost.resource == Resource.CLAY) {
                    clay = clay - cost.cost;
                }
                if (cost.resource == Resource.OBSIDIAN) {
                    obsidian = obsidian - cost.cost;
                }
                if (cost.resource == Resource.GEODE) {
                    geode = geode - cost.cost;
                }
            }
        }

        public int getResource(Resource resource) {
            if (resource == Resource.ORE) {
                return ore;
            } else if (resource == Resource.CLAY) {
                return clay;
            } else if (resource == Resource.OBSIDIAN) {
                return obsidian;
            } else if (resource == Resource.GEODE) {
                return geode;
            }
            return -1;
        }
        @Override
        public boolean equals(Object other) {
            if (other instanceof Path o) {
                return this.minute == o.minute
                    && this.ore == o.ore && this.clay == o.clay
                    && this.obsidian == o.obsidian && this.geode == o.geode
                    && this.oreRobots == o.oreRobots && this.clayRobots == o.clayRobots
                    && this.obsidianRobots == o.obsidianRobots && this.geodeRobots == o.geodeRobots;
            }
            return false;
        }

    }

    public static class Blueprint {

        List<Cost> oreRobot;
        List<Cost> clayRobot;
        List<Cost> obsidianRobot;
        List<Cost> geodeRobot;

        public Blueprint(String input) {
            final var costs = input.split(":")[1].split("\\.");

            oreRobot = Cost.parseCosts(costs[0]);
            clayRobot = Cost.parseCosts(costs[1]);
            obsidianRobot = Cost.parseCosts(costs[2]);
            geodeRobot = Cost.parseCosts(costs[3]);
        }

        public boolean canAffordOre(Path path) {
            for (var cost: oreRobot) {
               final var resourceAmount = path.getResource(cost.resource);
               if (resourceAmount < cost.cost) {
                   return false;
               }
            }
            return true;
        }

        public boolean canAffordClay(Path path) {
            for (var cost: clayRobot) {
                final var resourceAmount = path.getResource(cost.resource);
                if (resourceAmount < cost.cost) {
                    return false;
                }
            }
            return true;
        }

        public boolean canAffordObsidian(Path path) {
            for (var cost: obsidianRobot) {
                final var resourceAmount = path.getResource(cost.resource);
                if (resourceAmount < cost.cost) {
                    return false;
                }
            }
            return true;
        }

        public boolean canAffordGeode(Path path) {
            for (var cost: geodeRobot) {
                final var resourceAmount = path.getResource(cost.resource);
                if (resourceAmount < cost.cost) {
                    return false;
                }
            }
            return true;
        }

    }

    public record Cost(Resource resource, int cost) {

        public static List<Cost> parseCosts(String input) {
            final var costs = input.split("costs")[1].split("and");

            final var list = new ArrayList<Cost>();
            for (var entry : costs) {
                final var c = entry.split(" ");
                list.add(new Cost(Resource.getResource(c[2]), Integer.parseInt(c[1])));
            }
            return list;
        }
    }

    public enum Resource {
        ORE,
        CLAY,
        OBSIDIAN,
        GEODE;

        public static Resource getResource(String name) {
            final var caps = name.toUpperCase();
            return valueOf(caps);
        }
    }
}
