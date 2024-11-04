package uimp.muia.rpm;

import java.util.List;

public interface Replacement<I extends Individual> extends Stochastic {

    List<I> replace(List<I> population, List<I> candidates);

}
