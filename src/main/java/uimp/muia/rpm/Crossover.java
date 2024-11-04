package uimp.muia.rpm;

public interface Crossover<I> extends Stochastic {

    I apply(I left, I right);

}
