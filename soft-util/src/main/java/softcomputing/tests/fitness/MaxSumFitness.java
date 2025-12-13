package softcomputing.tests.fitness;

import softcomputing.genetic.chromosome.IntegerChromosome;
import softcomputing.utils.FitnessFunction;

public class MaxSumFitness implements FitnessFunction<IntegerChromosome> {

    @Override
    public double evaluate(IntegerChromosome chromosome) {
        int sum = 0;
        for (int i = 0; i < chromosome.length(); i++) {
            sum += chromosome.getGene(i);
        }
        return sum;
    }
}