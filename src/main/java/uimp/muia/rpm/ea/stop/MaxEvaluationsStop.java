package uimp.muia.rpm.ea.stop;

import uimp.muia.rpm.ea.EvolutionaryAlgorithm;
import uimp.muia.rpm.ea.Individual;
import uimp.muia.rpm.ea.Stop;

public class MaxEvaluationsStop<I extends Individual> implements Stop<I> {

    private final long max;

    private long current;

    public MaxEvaluationsStop(long max) {
        this.current = 0L;
        this.max = max;
    }

    @Override
    public boolean stop() {
        return current >= max;
    }

    @Override
    public void update(EvolutionaryAlgorithm<I> algorithm) {
        this.current = algorithm.getEvaluations();
    }
}
