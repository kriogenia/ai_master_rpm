package uimp.muia.rpm.ea.mutation;

import uimp.muia.rpm.ea.Mutation;
import uimp.muia.rpm.ea.Stochastic;
import uimp.muia.rpm.ea.phub.RandomAssignedHub;

import java.util.Random;

/**
 * Mutation for the RandomAssignedHub implementation where each node can receive a
 * new random assigned hub based on a probability
 */
public class RAHMutation implements Mutation<RandomAssignedHub.Individual>, Stochastic {

    private final double probability;

    private Random rand;

    public RAHMutation(double probability) {
        this.probability = probability;
        this.rand = new Random();
    }

    @Override
    public RandomAssignedHub.Individual apply(RandomAssignedHub.Individual individual) {
        var size = individual.size();
        var chromosome = individual.chromosome();
        for (var i = 0; i < size; i++)  {
            if (rand.nextDouble() < probability) {
                chromosome[i] = (byte) rand.nextInt(size);
            }
        }
        return new RandomAssignedHub.Individual(chromosome);
    }

    @Override
    public void setRandom(Random random) {
        this.rand = random;
    }
}
