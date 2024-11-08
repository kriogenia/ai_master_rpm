package uimp.muia.rpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uimp.muia.rpm.ea.EvolutionaryAlgorithm;
import uimp.muia.rpm.ea.crossover.FixedPSinglePointCrossover;
import uimp.muia.rpm.ea.mutation.ReassignHubMutation;
import uimp.muia.rpm.ea.phub.SubProblem;
import uimp.muia.rpm.ea.phub.USApHMP;
import uimp.muia.rpm.ea.replacement.ElitistReplacement;
import uimp.muia.rpm.ea.selection.BinaryTournament;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Performs a single execution of the algorithm until the stop criterion is met
 */
public class Run {

    private static final Logger LOG = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        var file = "subproblems/phub_10.2.txt"; // todo from args
        var subproblem = Run.class.getClassLoader().getResource(file);
        if (subproblem == null) {
            throw new IllegalArgumentException("subproblem not found");
        }

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
//        var costs = NodesProperties.costs(NodesProperties.Type.LOOSE, 10);
//        var caps = NodesProperties.capacities(NodesProperties.Type.LOOSE, 10);
//        var problem = new RandomAssignedHub(phub, costs, caps);
        var problem = new USApHMP(phub);
        LOG.atInfo().log("Loaded and instantiated problem {}", file);

        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                // todo add configurations
                .withMaxEvaluations(10_000)
                .withSeed(123L)
                .withSelector(new BinaryTournament<>())
                .withCrossover(new FixedPSinglePointCrossover())
//                .withCrossover(new SinglePointCrossover<>())
//                .withMutation(new RAHMutation(1.0 / phub.n()))
                .withMutation(new ReassignHubMutation(1.0 / phub.n()))
                .withReplacement(new ElitistReplacement<>())
                .build();

        var best = ea.run();
        System.out.println(Arrays.toString(best.chromosome()));
    }

}
