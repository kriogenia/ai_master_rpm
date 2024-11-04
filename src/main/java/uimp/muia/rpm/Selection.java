package uimp.muia.rpm;

import java.util.Collection;

public interface Selection<I extends Individual> extends Stochastic {

    I selectParent(Collection<I> individuals);

}
