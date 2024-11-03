package uimp.muia.rpm.phub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public record SubProblem(
        Coordinates[] nodes,
        Double[][] flows,
        int hubs,
        double collectionCost,
        double transferCost,
        double distributionCost
) {

    public int n() {
        return nodes.length;
    }

    public static SubProblem fromFile(Path path) throws IOException {
        var lines = Files.readAllLines(path);

        var numNodes = Integer.parseInt(lines.getFirst());
        var nodes = lines.stream().skip(1).limit(numNodes).map(Coordinates::parse).toArray(Coordinates[]::new);
        var flows = lines.stream().skip(1 + numNodes).limit(numNodes)
                .map(SubProblem::parseDoubles)
                .toArray(Double[][]::new);
        var hubs = Integer.parseInt(lines.get(1 + 2 * numNodes));
        var rest = lines.stream().skip(2 + 2L * numNodes).map(Double::parseDouble).iterator();

        return new SubProblem(nodes, flows, hubs, rest.next(), rest.next(), rest.next());
    }

    private static Double[] parseDoubles(String line) {
        return Arrays.stream(line.split(" ")).map(Double::parseDouble).toArray(Double[]::new);
    }

    record Coordinates(double x, double y) {
        static Coordinates parse(String line) {
            var splits = parseDoubles(line);
            return new Coordinates(splits[0], splits[1]);
        }
    }

}

