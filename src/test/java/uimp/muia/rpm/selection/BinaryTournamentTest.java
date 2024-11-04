package uimp.muia.rpm.selection;

import org.junit.jupiter.api.Test;
import uimp.muia.rpm.individual.TestIndividual;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTournamentTest {

    @Test
    void selectParent() {
        var bt = new BinaryTournament<TestIndividual>();
        var individuals = List.of(
                new TestIndividual(1.0),
                new TestIndividual(2.0),
                new TestIndividual(3.0)
        );

        var rand = new Random();
        rand.setSeed(1L);
        var first = individuals.get(rand.nextInt(individuals.size()));
        var second = individuals.get(rand.nextInt(individuals.size()));
        assertNotEquals(first, second); // if equals, use different seed
        var expectedWinner = (first.fitness() >= second.fitness()) ? first : second;

        rand.setSeed(1L);
        bt.setRandom(rand);
        var selected = bt.selectParent(individuals);

        assertEquals(expectedWinner, selected);
    }
}