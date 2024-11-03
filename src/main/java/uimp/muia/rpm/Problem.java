package uimp.muia.rpm;

import java.util.Random;

/**
 * Version of es.uma.informatica.misia.ae.simpleea.Problem with generic to support different Individual
 * implementations without casting
 *
 * @param <I> Individual
 */
public interface Problem<I extends Individual> {

    double evaluate(I individual);
    I generateRandomIndividual(Random random);

}
