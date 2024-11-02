package uimp.muia.rpm.phub;

import es.uma.informatica.misia.ae.simpleea.Individual;
import es.uma.informatica.misia.ae.simpleea.Problem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

public record PHub(
        Coordinates[] nodes,
        Double[][] flows,
        int hubs,
        double collectionCost,
        double transferCost,
        double distributionCost
) implements Problem {

    public int n() {
        return nodes.length;
    }

    public static PHub fromFile(Path path) throws IOException {
        var lines = Files.readAllLines(path);

        var numNodes = Integer.parseInt(lines.getFirst());
        var nodes = lines.stream().skip(1).limit(numNodes).map(Coordinates::parse).toArray(Coordinates[]::new);
        var flows = lines.stream().skip(1 + numNodes).limit(numNodes)
                .map(PHub::parseDoubles)
                .toArray(Double[][]::new);
        var hubs = Integer.parseInt(lines.get(1 + 2 * numNodes));
        var rest = lines.stream().skip(2 + 2L * numNodes).map(Double::parseDouble).iterator();

        return new PHub(nodes, flows, hubs, rest.next(), rest.next(), rest.next());
    }

    private static Double[] parseDoubles(String line) {
        return Arrays.stream(line.split(" ")).map(Double::parseDouble).toArray(Double[]::new);
    }

    @Override
    public double evaluate(Individual individual) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Individual generateRandomIndividual(Random random) {
        throw new RuntimeException("Not implemented yet");
    }

    record Coordinates(double x, double y) {
        static Coordinates parse(String line) {
            var splits = parseDoubles(line);
            return new Coordinates(splits[0], splits[1]);
        }
    }

}

