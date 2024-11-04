package uimp.muia.rpm;

import uimp.muia.rpm.phub.NodesProperties;
import uimp.muia.rpm.phub.RandomAssignedHub;
import uimp.muia.rpm.phub.SubProblem;
import uimp.muia.rpm.selection.BinaryTournament;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        var subproblem = Main.class.getClassLoader().getResource("subproblems/phub1_10.3.txt");
        if (subproblem == null) {
            throw new IllegalArgumentException("subproblem not found");
        }

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
        var costs = NodesProperties.costs(NodesProperties.Type.LOOSE, 10);
        var caps = NodesProperties.capacities(NodesProperties.Type.LOOSE, 10);
        var problem = new RandomAssignedHub(phub, costs, caps);

        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                // todo add configurations
                .withSeed(123L)
                .withSelector(new BinaryTournament<>())
                .build();
        var best = ea.run();
        System.out.printf("BEST SOLUTION FOUND: %s", best);
    }

}
