package uimp.muia.rpm;

public interface Mutation<I extends Individual> {

    I apply(I individual);

}
