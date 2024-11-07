package uimp.muia.rpm.ea.phub;

import uimp.muia.rpm.ea.Individual;
import uimp.muia.rpm.ea.Problem;
import uimp.muia.rpm.ea.individual.FixedPAssignedHub;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class USApHMP implements Problem<FixedPAssignedHub> {

    private final SubProblem scenario;

    private Random random;

    public USApHMP(SubProblem scenario) {
        this.scenario = scenario;
        this.random = new Random();
    }

    @Override
    public double evaluate(FixedPAssignedHub individual) {
        var n = scenario.n();
        var assignedHubs = individual.chromosome();

        var totalShippingsCost = 0.0;
        for (var i = 0; i < n; i++) {
            for (var j = 0; j < n; j++) {
                totalShippingsCost += scenario.flowBetween(i, j) * costToShip(assignedHubs, i, j);
            }
        }
        return 0 - totalShippingsCost;
    }

    @Override
    public FixedPAssignedHub generateRandomIndividual() {
        var n = scenario.n();
        var p = scenario.p();

        var hubs = random.ints(0, n)
                .boxed()
                .distinct()
                .limit(3)
                .toList();

        var chromosome = new Byte[n];
        IntStream.range(0, n).forEach(i -> chromosome[i] = (byte)(int)hubs.get(random.nextInt(p)));
        hubs.forEach(i -> chromosome[i] = (byte)(int)i);

        return new FixedPAssignedHub(p, chromosome);
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    // visible for testing
    double costToShip(Byte[] assignedHubs, int from, int to) {
        var fromAssignedHub = assignedHubs[from];
        var toAssignedHub = assignedHubs[to];

        var collectCost = scenario.distanceBetween(from, fromAssignedHub) * scenario.collectionCost();
        var transferCost = scenario.distanceBetween(fromAssignedHub, toAssignedHub) * scenario.transferCost();
        var deliveryCost = scenario.distanceBetween(toAssignedHub, to) * scenario.distributionCost();

        return collectCost + transferCost + deliveryCost;
    }

}
