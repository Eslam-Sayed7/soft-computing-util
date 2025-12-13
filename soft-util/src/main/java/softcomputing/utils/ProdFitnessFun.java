package softcomputing.utils;

import softcomputing.genetic.chromosome.Chromosome;

public class ProdFitnessFun  implements FitnessFunction<Chromosome<?>> {

    @Override
    public double evaluate(Chromosome<?> chromosome) {
        double fitness = 1.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            double gene = (Double) chromosome.getGene(i);
            fitness *= gene;
        }
        return fitness;
    }
}
