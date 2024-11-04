package uimp.muia.rpm;

/**
 * Version of es.uma.informatica.misia.ae.simpleea.Individual to use with the new generic approach,
 * automatically implements `Comparable` to compare individuals using the fitness
 */
public interface Individual extends Comparable<Individual> {

    void fitness(double fitness);
    double fitness();

    @Override
    default int compareTo(Individual o) {
        return Double.compare(fitness(), o.fitness());
    }

}
