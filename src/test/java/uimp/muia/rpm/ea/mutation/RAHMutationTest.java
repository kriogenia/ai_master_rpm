package uimp.muia.rpm.ea.mutation;

import org.junit.jupiter.api.Test;
import uimp.muia.rpm.ea.phub.RandomAssignedHub;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RAHMutationTest {

    @Test
    void apply() {
        var rand = new Random(13L);
        var rahm = new RAHMutation(0.5);
        rahm.setRandom(rand);

        var ind = new RandomAssignedHub.Individual(new Byte[]{ 1, 2, 3, 4, 5, 6 });
        var mutated = rahm.apply(ind.replica());

        assertNotEquals(ind.chromosome(), mutated.chromosome());
    }
}