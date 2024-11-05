package uimp.muia.rpm.individual;

import uimp.muia.rpm.Individual;

public class TestIndividual implements Individual {

    private double fitness = 0.0;
    private Byte[] chromosome = new Byte[10];

    public TestIndividual(double fitness) {
        this.fitness = fitness;
    }

    public TestIndividual(Byte[] chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public void fitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double fitness() {
        return fitness;
    }

    @Override
    public Byte[] chromosome() {
        return chromosome;
    }

    @Override
    public void setChromosome(Byte[] chromosome) {
        this.chromosome = chromosome;
    }

    @Override
    public Individual replica() {
        return new TestIndividual(chromosome);
    }

}
