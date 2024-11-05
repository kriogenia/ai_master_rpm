package uimp.muia.rpm.mutation;

import org.junit.jupiter.api.Test;
import uimp.muia.rpm.phub.RandomAssignedHub;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RAHMutationTest {

    @Test
    void mutate() {
        var rand = new Random(13L);
        var rahm = new RAHMutation(0.5);
        rahm.setRandom(rand);

        var ind = new RandomAssignedHub.Individual(new Byte[]{ 1, 2, 3, 4, 5, 6 });
        var mutated = rahm.mutate(ind.replica());

        System.out.println(ind);
        System.out.println(mutated);
        assertNotEquals(ind.chromosome(), mutated.chromosome());
    }
}