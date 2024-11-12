package uimp.muia.rpm.ea.stop;

import uimp.muia.rpm.ea.EvolutionaryAlgorithm;
import uimp.muia.rpm.ea.Stop;
import uimp.muia.rpm.ea.individual.FixedPAssignedHub;

import java.util.Arrays;

public class BestSolutionStop implements Stop<FixedPAssignedHub> {

    private final FixedPAssignedHub objective;

    private boolean found;

    public BestSolutionStop(int p, Byte[] allocations) {
        this.objective = new FixedPAssignedHub(p, Arrays.stream(allocations).map(x -> (byte)(x - 1)).toArray(Byte[]::new));
        this.found = false;
    }

    @Override
    public boolean stop() {
        return found;
    }

    @Override
    public void update(EvolutionaryAlgorithm<FixedPAssignedHub> algorithm) {
        this.found = objective.equals(algorithm.getBest());
    }

}
