package uimp.muia.rpm.phub;

import es.uma.informatica.misia.ae.simpleea.Problem;

import java.util.*;

public class RandomAssignedHub implements Problem {

    private final SubProblem scenario;
    private final NodesProperties costs;
    private final NodesProperties caps;

    public RandomAssignedHub(SubProblem scenario, NodesProperties costs, NodesProperties caps) {
        this.scenario = scenario;
        this.costs = costs;
        this.caps = caps;
    }

    @Override
    public double evaluate(es.uma.informatica.misia.ae.simpleea.Individual individual) {
        return 0;
    }

    @Override
    public es.uma.informatica.misia.ae.simpleea.Individual generateRandomIndividual(Random random) {
        return new Individual(scenario.n()).randomize(random);
    }

    // VisibleForTest
    static class Individual extends es.uma.informatica.misia.ae.simpleea.Individual {

        final Byte[] assignedHubs;

        Individual(int size) {
            this.assignedHubs = new Byte[size];
        }

        Individual randomize(Random random) {
            var target = random.nextInt(assignedHubs.length);

            var uniqueHubs = new HashSet<Byte>();
            while (uniqueHubs.size() < target) {
                uniqueHubs.add((byte) random.nextInt(assignedHubs.length));
            }
            var hubs = uniqueHubs.toArray(Byte[]::new);

            for (int i = 0; i < assignedHubs.length; i++) {
                assignedHubs[i] = hubs[random.nextInt(target)];
            }
            Arrays.stream(hubs).forEach(i -> assignedHubs[i] = i);

            return this;
        }

        @Override
        public String toString() {
            return "%s -> %f".formatted(Arrays.toString(assignedHubs), super.fitness);
        }
    }

}
