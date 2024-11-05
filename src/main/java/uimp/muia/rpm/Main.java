package uimp.muia.rpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uimp.muia.rpm.crossover.SinglePointCrossover;
import uimp.muia.rpm.mutation.RAHMutation;
import uimp.muia.rpm.phub.NodesProperties;
import uimp.muia.rpm.phub.RandomAssignedHub;
import uimp.muia.rpm.phub.SubProblem;
import uimp.muia.rpm.replacement.ElitistReplacement;
import uimp.muia.rpm.selection.BinaryTournament;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        var file = "subproblems/phub1_10.3.txt"; // todo from args
        var subproblem = Main.class.getClassLoader().getResource(file);
        if (subproblem == null) {
            throw new IllegalArgumentException("subproblem not found");
        }

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
        var costs = NodesProperties.costs(NodesProperties.Type.LOOSE, 10);
        var caps = NodesProperties.capacities(NodesProperties.Type.LOOSE, 10);
        var problem = new RandomAssignedHub(phub, costs, caps);
        LOG.atInfo().log("Loaded and instantiated problem {}", file);


//        var test = new Byte[]{ 2, 3, 2, 3, 6, 3, 6, 6, 6, 6 };
//        var fitness = problem.evaluate(new RandomAssignedHub.Individual(test));
//        System.out.println(fitness);

        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                // todo add configurations
                .withMaxEvaluations(1_000)
                .withSeed(123L)
                .withSelector(new BinaryTournament<>())
                .withCrossover(new SinglePointCrossover<>())
                .withMutation(new RAHMutation(1.0 / phub.n()))
                .withReplacement(new ElitistReplacement<>())
                .build();
        var best = ea.run();
        System.out.printf("BEST SOLUTION FOUND: %s%n", best);
    }

}
