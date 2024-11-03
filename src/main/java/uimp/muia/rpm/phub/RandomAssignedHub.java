package uimp.muia.rpm.phub;

import uimp.muia.rpm.GenericProblem;

import java.util.*;

public class RandomAssignedHub extends GenericProblem<RandomAssignedHub.Individual> {

    private final SubProblem scenario;
    private final NodesProperties costs;
    private final NodesProperties caps;

    public RandomAssignedHub(SubProblem scenario, NodesProperties costs, NodesProperties caps) {
        this.scenario = scenario;
        this.costs = costs;
        this.caps = caps;
    }

    @Override
    protected double eval(Individual individual) {
        var n = scenario.n();
        var totalShippingsCost = 0.0;
        for (var i = 0; i < n; i++) {
            for (var j = 0; j < n; j++) {
                totalShippingsCost += costToShip(individual.assignedHubs, i, j);
            }
        }
        var buildingCost = individual.hubs.stream().map(costs::get).reduce(0.0, Double::sum);
        var totalCapacity = individual.hubs.stream().map(caps::get).reduce(0.0, Double::sum);
        return totalCapacity / (totalShippingsCost + buildingCost);
    }

    @Override
    public es.uma.informatica.misia.ae.simpleea.Individual generateRandomIndividual(Random random) {
        return new Individual(scenario.n()).randomize(random);
    }

    // visible for testing
    double costToShip(Byte[] assignedHubs, int from, int to) {
        var fromAssignedHub = assignedHubs[from];
        var toAssignedHub = assignedHubs[to];
        // todo calculate distances
        var collectCost = scenario.flows()[from][fromAssignedHub] * scenario.collectionCost();
        var transferCost = scenario.flows()[fromAssignedHub][toAssignedHub] * scenario.transferCost();
        var deliveryCost = scenario.flows()[toAssignedHub][to] * scenario.distributionCost();
        return collectCost + transferCost + deliveryCost;
    }

    static public class Individual extends es.uma.informatica.misia.ae.simpleea.Individual {

        List<Byte> hubs;
        final Byte[] assignedHubs;

        Individual(int size) {
            this.hubs = new ArrayList<>();
            this.assignedHubs = new Byte[size];
        }

        Individual(Byte[] assignedHubs) {
            this.assignedHubs = assignedHubs;
            this.hubs = Arrays.stream(assignedHubs).distinct().toList();
        }

        Individual randomize(Random random) {
            var target = random.nextInt(assignedHubs.length);

            var uniqueHubs = new HashSet<Byte>();
            while (uniqueHubs.size() < target) {
                uniqueHubs.add((byte) random.nextInt(assignedHubs.length));
            }
            hubs = uniqueHubs.stream().toList();

            for (int i = 0; i < assignedHubs.length; i++) {
                assignedHubs[i] = hubs.get(random.nextInt(target));
            }
            hubs.forEach(i -> assignedHubs[i] = i);

            return this;
        }

        @Override
        public String toString() {
            return "%s -> %f".formatted(Arrays.toString(assignedHubs), super.fitness);
        }
    }

}
