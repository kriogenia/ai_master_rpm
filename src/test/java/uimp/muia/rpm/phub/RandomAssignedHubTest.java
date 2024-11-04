package uimp.muia.rpm.phub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomAssignedHubTest {

    private final Random rand = new Random();
    private final long SEED = 123;

    private final double DELTA = 0.001;
    private final int NODES = 3;

    private final RandomAssignedHub rah = newProblem();

    @BeforeEach
    void setUp() {
        rand.setSeed(SEED);
        rah.setRandom(rand);
    }

    @Test
    void generateRandomIndividual() {
        var hubs = rand.nextInt(NODES) + 1;
        rand.setSeed(SEED);
        rah.setRandom(rand);
        var individual = rah.generateRandomIndividual();

        assertEquals(NODES, individual.assignedHubs.length);
        assertTrue(Arrays.stream(individual.assignedHubs).allMatch(x -> x <= NODES));
        var selectedHubs = Arrays.stream(individual.assignedHubs).distinct().toList();
        assertEquals(hubs, selectedHubs.size());
        selectedHubs.forEach(h -> individual.assignedHubs[h] = h);
    }

    @Test
    void evaluate() {
        var individual = new RandomAssignedHub.Individual(new Byte[]{1, 1, 1});
        var fitness = rah.evaluate(individual);
        assertEquals(0.1290, fitness, DELTA);
    }

    @Test
    void costToShip() {
        var cost = rah.costToShip(new Byte[]{ 1, 1, 1 }, 0, 2);
        assertEquals(1.0, cost, DELTA);
    }

    private RandomAssignedHub newProblem() {
        var coordinates = new SubProblem.Coordinates[]{
                new SubProblem.Coordinates(0.0, 2.0),
                new SubProblem.Coordinates(2.0, 3.0),
                new SubProblem.Coordinates(4.0, 4.0)
        };
        var flows = new Double[][]{
                new Double[]{ 0.0, 1.0, 0.5 },
                new Double[]{ 1.0, 0.0, 0.5 },
                new Double[]{ 0.5, 0.5, 0.0 },
        };
        var scenario = new SubProblem(coordinates, flows, 0, 0.5, 0.25, 1.0);
        var costs = new NodesProperties(NodesProperties.Type.LOOSE, new Double[]{ 2.0, 1.0, 1.5 });
        var caps = new NodesProperties(NodesProperties.Type.LOOSE, new Double[]{ 3.0, 1.0, 2.0 });
        return new RandomAssignedHub(scenario, costs, caps);
    }

}