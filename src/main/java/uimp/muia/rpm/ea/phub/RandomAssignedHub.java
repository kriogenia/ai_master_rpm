package uimp.muia.rpm.ea.phub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uimp.muia.rpm.ea.Problem;

import java.util.*;

public class RandomAssignedHub implements Problem<RandomAssignedHub.Individual> {

    private static final Logger LOG = LoggerFactory.getLogger(RandomAssignedHub.class);

    private final SubProblem scenario;
    private final NodesProperties costs;
    private final NodesProperties caps;

    private Random random;

    public RandomAssignedHub(SubProblem scenario, NodesProperties costs, NodesProperties caps) {
        this.scenario = scenario;
        this.costs = costs;
        this.caps = caps;
    }

    @Override
    public double evaluate(Individual individual) {
        var n = scenario.n();
        var totalShippingsCost = 0.0;
        for (var i = 0; i < n; i++) {
            for (var j = 0; j < n; j++) {
                totalShippingsCost += scenario.flowBetween(i, j) * costToShip(individual.assignedHubs, i, j); // Xijkl
            }
        }
        var buildingCost = individual.hubs.stream().map(costs::get).reduce(0.0, Double::sum);
        // capacities
        return 0 - (totalShippingsCost + buildingCost);
    }

    @Override
    public Individual generateRandomIndividual() {
        var size = scenario.n();
        var target = random.nextInt(size) + 1;

        var uniqueHubs = new HashSet<Byte>();
        while (uniqueHubs.size() < target) {
            uniqueHubs.add((byte) random.nextInt(size));
        }
        var hubs = uniqueHubs.toArray(Byte[]::new);

        var assignedHubs = new Byte[size];
        for (var i = 0; i < size; i++) {
            assignedHubs[i] = hubs[random.nextInt(target)];
        }
        Arrays.stream(hubs).forEach(i -> assignedHubs[i] = i);

        LOG.atDebug().log(Arrays.toString(assignedHubs));
        return new Individual(assignedHubs);
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

    static public class Individual implements uimp.muia.rpm.ea.Individual {

        double fitness;
        List<Byte> hubs;
        final Byte[] assignedHubs;

        public Individual(Byte[] assignedHubs) {
            this.fitness = 0;
            this.assignedHubs = assignedHubs;
            this.hubs = Arrays.stream(assignedHubs).distinct().toList();
        }

        @Override
        public void fitness(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public double fitness() {
            return fitness;
        }

        @Override
        public Byte[] chromosome() {
            return this.assignedHubs;
        }

        @Override
        public Individual replica() {
            return new Individual(this.chromosome().clone());
        }

        @Override
        public String toString() {
            return "%s -> %f".formatted(Arrays.toString(assignedHubs), this.fitness);
        }
    }

}
