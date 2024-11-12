package uimp.muia.rpm.ea;

public interface Stop<I extends Individual> {

    boolean stop();
    void update(EvolutionaryAlgorithm<I> algorithm);

}
