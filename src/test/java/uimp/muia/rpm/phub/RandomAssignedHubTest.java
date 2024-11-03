package uimp.muia.rpm.phub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomAssignedHubTest {

    private final Random rand = new Random();
    private final long SEED = 123;

    @BeforeEach
    void setUp() {
        rand.setSeed(SEED);
    }

    @Test
    void generateRandomIndividual() {
        var nodes = 10;
        var hubs = rand.nextInt(nodes);
        rand.setSeed(SEED);

        var scenario = new SubProblem(new SubProblem.Coordinates[nodes], new Double[nodes][nodes], 3, 1.0, 1.0, 1.0);
        var rah = new RandomAssignedHub(scenario, null);
        var individual = (RandomAssignedHub.Individual) rah.generateRandomIndividual(rand);

        assertEquals(nodes, individual.assignedHubs.length);
        assertTrue(Arrays.stream(individual.assignedHubs).allMatch(x -> x <= nodes));
        var selectedHubs = Arrays.stream(individual.assignedHubs).distinct().toList();
        assertEquals(hubs, selectedHubs.size());
        selectedHubs.forEach(h -> individual.assignedHubs[h] = h);
    }
}