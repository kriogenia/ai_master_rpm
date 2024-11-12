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
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Benchmark {

    private static final int BASE_SEED = 19;
    private static final int EXECUTIONS = 30;
    private static final int N_SOLUTIONS = 20;
    private static final int MAX_EVALUATIONS = 100_000;
    private static final int POPULATION_SIZE = 10;

    private static final List<Double> MUTATIONS = List.of(0.05, 0.1, 0.25, 0.5);

    public static void main(String[] args) throws Exception {
        var solutions = loadSolutions();
        System.out.println("| n | p | mutation | population | objective | gap | evaluations | time |");
        System.out.println("|---|---|----------|------------|-----------|-----|-------------|------|");
        for (var solution : solutions) {
            for (var mutation : MUTATIONS) {
                var measurements = new ArrayList<Measurement>();
                for (int i = 0; i < EXECUTIONS; i++) {
                    var result = run(solution, BASE_SEED + i, mutation);
                    measurements.add(result);
                }
                var results = calculateAverages(solution, measurements);

                System.out.printf("| %d | %d | %.2f | - | %f | %.2f | %d | %.3f ms |%n" ,
                        solution.n, solution.p, mutation, solution.objective,
                        results.gapPercent, results.evaluations, results.time);
            }
        }
    }

    private static Measurement run(Solution solution, long seed, double mutation) throws Exception {
        var file = "subproblems/phub_%d.%d.txt".formatted(solution.n, solution.p);
        var subproblem = Benchmark.class.getClassLoader().getResource(file);
        assert subproblem != null;

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
        var problem = new USApHMP(phub);
        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                .withSeed(seed)
                .withPopulationSize(POPULATION_SIZE)
                .withStop(new OrStop<FixedPAssignedHub>()
                        .add(new BestSolutionStop(solution.p, solution.allocation))
                        .add(new MaxEvaluationsStop<>(MAX_EVALUATIONS)))
//                .withMaxEvaluations(args.limit())
                .withSelector(new BinaryTournament<>())
                .withCrossover(new FixedPSinglePointCrossover())
                .withMutation(new ReassignHubMutation(mutation)) // TODO var mutation
                .withReplacement(new ElitistReplacement<>())
                .build();

        var start = Instant.now();
        var best = ea.run();
        var elapsed = Duration.between(start, Instant.now()).toNanos() / 1_000_000.0;

        return new Measurement(best.fitness(), ea.getEvaluations(), elapsed);
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

    private static Averages calculateAverages(Solution solution, List<Measurement> measurements) {
        var avgFitness = - measurements.stream().map(Measurement::fitness).reduce(0.0, Double::sum) / EXECUTIONS;
        var avgTime = measurements.stream().map(Measurement::time).reduce(0.0, Double::sum) / EXECUTIONS;
        var avgEvals = measurements.stream().map(Measurement::evaluations).reduce(0L, Long::sum) / EXECUTIONS;
        var gapPercent = (1 - solution.objective / avgFitness) * 100;

        return new Averages(gapPercent, avgEvals, avgTime);
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

    record Measurement(double fitness, long evaluations, double time) {}

    record Averages(double gapPercent, long evaluations, double time) {}

}
