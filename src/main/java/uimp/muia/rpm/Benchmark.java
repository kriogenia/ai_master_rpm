package uimp.muia.rpm;

import uimp.muia.rpm.ea.EvolutionaryAlgorithm;
import uimp.muia.rpm.ea.Individual;
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

    private static final List<Double> MUTATIONS = List.of(0.05, 0.1, 0.25, 0.5);

    public static void main(String[] args) throws Exception {
        var solutions = loadSolutions();

        var mode = args.length > 0 && args[0].equals("optimal") ? Mode.OPTIMAL : Mode.EVALUATIONS;
        mode.printHeader();

        for (var solution : solutions) {
            for (var mutation : MUTATIONS) {
                var measurements = new ArrayList<Result>();
                for (int i = 0; i < EXECUTIONS; i++) {
                    var result = run(solution, BASE_SEED + i, mutation);
                    measurements.add(result);
                }
                mode.printResults(solution, mutation, measurements);
            }
        }
    }

    private static Result run(Solution solution, long seed, double mutation) throws Exception {
        var file = "subproblems/phub_%d.%d.txt".formatted(solution.n, solution.p);
        var subproblem = Benchmark.class.getClassLoader().getResource(file);
        assert subproblem != null;

        var phub = SubProblem.fromFile(Path.of(subproblem.toURI()));
        var problem = new USApHMP(phub);
        var ea = new EvolutionaryAlgorithm.Builder<>(problem)
                .withSeed(seed)
                .withPopulationSize(solution.n + 1)
                .withStop(new OrStop<FixedPAssignedHub>()
                        .add(new BestSolutionStop(solution.p, solution.allocation))
                        .add(new MaxEvaluationsStop<>(MAX_EVALUATIONS)))
                .withSelector(new BinaryTournament<>())
                .withCrossover(new FixedPSinglePointCrossover())
                .withMutation(new ReassignHubMutation(mutation))
                .withReplacement(new ElitistReplacement<>())
                .build();

        var start = Instant.now();
        var best = ea.run();
        var elapsed = Duration.between(start, Instant.now()).toNanos();

        return new Result(best, ea.getEvaluations(), elapsed);
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

        Solution {
            // the provided solutions are based on index starting at 1
            allocation = Arrays.stream(allocation).map(x -> (byte)(x - 1)).toArray(Byte[]::new);
        }

        @Override
        public int compareTo(Solution o) {
            return (this.n + this.p) - (o.n + o.p);
        }
    }

    record Result(FixedPAssignedHub best, long evaluations, long time) {}

    enum Mode {
        OPTIMAL,
        EVALUATIONS;

        void printHeader() {
            var header = switch (this) {
                case OPTIMAL -> "n,p,mutation,objective,best,gap,hit_rate,evaluations,time";
                case EVALUATIONS -> "not yet implemented";
            };
            System.out.println(header);
        }

        private static final String TAIL_TMP = "%.0f,%.6f,%.3f,%d,%d";

        void printResults(Solution solution, double mutation, List<Result> results) {
            var solutions = results.stream().map(Result::best).toList();
            var hits = results.stream().filter(s -> Arrays.equals(solution.allocation, s.best.chromosome())).toList();

            var tail = switch (hits.size()) {
                case 0 -> {
                    var best = -solutions.stream().max(Individual::compareTo).orElseThrow().fitness();
                    var gap = Math.abs(1 - solution.objective / best);
                    var time = results.stream().map(Result::time).reduce(0L, Long::sum) / results.size();
                    yield TAIL_TMP.formatted(best, gap, 0.0, MAX_EVALUATIONS, time);
                }
                default -> {
                    var best = -hits.getFirst().best.fitness();
                    var hitRate = ((double) hits.size()) / results.size();
                    var evaluations = hits.stream().map(Result::evaluations).reduce(0L, Long::sum) / hits.size();
                    var time = hits.stream().map(Result::time).reduce(0L, Long::sum) / hits.size();
                    yield TAIL_TMP.formatted(best, 0.0, hitRate, evaluations, time);
                }
            };

            System.out.printf("%d,%d,%.2f,%.0f,%s%n", solution.n, solution.p, mutation, solution.objective, tail);
        }

    }


}
