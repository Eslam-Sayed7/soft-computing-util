package softcomputing.tests.fitness;

import softcomputing.genetic.chromosome.BinaryChromosome;
import softcomputing.utils.FitnessFunction;

public class DecFromBinFitness  implements  FitnessFunction<BinaryChromosome> {

    @Override
    public double evaluate(BinaryChromosome chromosome) {
        double fitness = 0.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            int gene = (Integer) chromosome.getGene(i);
            fitness += gene * Math.pow(2, len - i - 1);
        }
        return fitness;
    }
}
