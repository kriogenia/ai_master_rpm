package uimp.muia.rpm.individual;

import uimp.muia.rpm.Individual;

public class TestIndividual implements Individual {

    private double fitness;

    public TestIndividual(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public void fitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double fitness() {
        return fitness;
    }
}
