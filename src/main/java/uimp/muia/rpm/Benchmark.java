package uimp.muia.rpm;

import uimp.muia.rpm.ea.EvolutionaryAlgorithm;
import uimp.muia.rpm.ea.crossover.FixedPSinglePointCrossover;
import uimp.muia.rpm.ea.individual.FixedPAssignedHub;
import uimp.muia.rpm.ea.mutation.ReassignHubMutation;
import uimp.muia.rpm.ea.phub.SubProblem;
import uimp.muia.rpm.ea.phub.USApHMP;
import uimp.muia.rpm.ea.replacement.ElitistReplacement;
import uimp.muia.rpm.ea.selection.BinaryTournament;
import uimp.muia.rpm.ea.stop.BestSolutionStop;
import uimp.muia.rpm.ea.stop.MaxEvaluationsStop;
import uimp.muia.rpm.ea.stop.OrStop;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

public class Benchmark {

    public static final int N_SOLUTIONS = 20;
    public static final int POPULATION_SIZE = 10;

    public static void main(String[] args) throws Exception {
        var solutions = loadSolutions();
        for (var solution : solutions) {
            System.out.printf("Looking for %d,%d. Start at %s%n", solution.n, solution.p, Instant.now());
            run(solution);
        }
    }

    private static void run(Solution solution) throws URISyntaxException, IOException {
        var file = "subproblems/phub_%d.%d.txt".formatted(solution.n, solution.p);
        var subproblem = Benchmark.class.getClassLoader().getResource(file);
        assert subproblem != null;

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
        var problem = new USApHMP(phub);
        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                .withSeed(123L) // TODO pass seed
                .withPopulationSize(POPULATION_SIZE)
                .withStop(new OrStop<FixedPAssignedHub>()
                        .add(new BestSolutionStop(solution.p, solution.allocation))
                        .add(new MaxEvaluationsStop<>(10_000L)))
//                .withMaxEvaluations(args.limit())
                .withSelector(new BinaryTournament<>())
                .withCrossover(new FixedPSinglePointCrossover())
                .withMutation(new ReassignHubMutation(1.0 / phub.n())) // TODO var mutation
                .withReplacement(new ElitistReplacement<>())
                .build();
        var best = ea.run();
        System.out.printf("Gap: [%f%%]%n", 100 - 100 * solution.objective/(-best.fitness()));
    }


    private static Set<Solution> loadSolutions() throws URISyntaxException, IOException {
        var phub3 = Benchmark.class.getClassLoader().getResource("or-library/phub3.txt");
        assert phub3 != null;
        var lines = Files.readAllLines(Path.of(phub3.toURI())).stream().skip(695).iterator();

        var solutions = new TreeSet<Solution>();
        while (solutions.size() < N_SOLUTIONS) {
            var header = lines.next();
            var n = Integer.parseInt(header.substring(15, 17));
            var p = Integer.parseInt(header.substring(21, 22));
            var objective = Double.parseDouble(lines.next().replace("Objective  : ", ""));
            var splits = lines.next().replace("Allocation : ", "").split(", ");
            var allocations = Arrays.stream(splits).map(Integer::parseInt).map(i -> (byte)(int)i).toArray(Byte[]::new);
            solutions.add(new Solution(n, p, objective, allocations));
            lines.next();
        }

        return solutions;
    }

    record Solution(
            int n,
            int p,
            double objective,
            Byte[] allocation
    ) implements Comparable<Solution> {
        @Override
        public int compareTo(Solution o) {
            return (this.n + this.p) - (o.n + o.p);
        }
    }


}
