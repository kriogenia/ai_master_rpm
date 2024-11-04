package uimp.muia.rpm;

public interface Mutation<I extends Individual> extends Stochastic {

    I mutate(I individual);

}
