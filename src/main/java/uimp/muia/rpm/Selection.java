package uimp.muia.rpm;

import java.util.List;

public interface Selection<I extends Individual> extends Stochastic {

    I selectParent(List<I> individuals);

}
