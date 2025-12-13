package softcomputing.utils;

import softcomputing.genetic.chromosome.Chromosome;

public class SumFitnessFun  implements FitnessFunction<Chromosome<?>> {

    @Override
    public double evaluate(Chromosome<?> chromosome) {
        double fitness = 0.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            int gene = (Integer) chromosome.getGene(i);
            fitness += gene;
        }
        return fitness;
    }
    
}
