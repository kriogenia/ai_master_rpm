package uimp.muia.rpm;

import es.uma.informatica.misia.ae.simpleea.Individual;
import es.uma.informatica.misia.ae.simpleea.Problem;

public abstract class GenericProblem<I extends Individual> implements Problem {

    @Override
    public double evaluate(Individual individual) {
        //noinspection unchecked
        return eval((I)individual); // evading instanceof for optimization
    }

    protected abstract double eval(I individual);
}
