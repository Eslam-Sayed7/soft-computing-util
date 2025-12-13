package softcomputing.tests.fitness;

import softcomputing.genetic.chromosome.FloatingPointChromosome;
import softcomputing.utils.FitnessFunction;

public class MaxProductFitness implements FitnessFunction<FloatingPointChromosome> {

    @Override
    public double evaluate(FloatingPointChromosome chromosome) {
        double product = 1.0;
        for (int i = 0; i < chromosome.length(); i++) {
            product *= chromosome.getGene(i);
        }
        return product;
    }
}