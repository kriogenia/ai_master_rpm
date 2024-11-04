package uimp.muia.rpm.crossover;

import uimp.muia.rpm.Crossover;
import uimp.muia.rpm.Individual;

import java.util.Random;

public class SinglePointCrossover<I extends Individual> implements Crossover<I> {

    private Random rand;

    public SinglePointCrossover() {
        this.rand = new Random();
    }

    @Override
    public I apply(I left, I right) {
        var size = left.size();
        var child =  left.replica();
        var cutPoint = rand.nextInt(size + 1);
        for (var i = cutPoint; i < size; i++) {
            child.chromosome()[i] = right.chromosome()[i];
        }
        //noinspection unchecked
        return (I) child; // ensure to be cast-able to same type as the parents
    }

    @Override
    public void setRandom(Random random) {
        this.rand = random;
    }
}