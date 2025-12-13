package softcomputing.genetic.chromosome.Factories;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.IntegerChromosome;

public class IntegerChromosomeFactory implements ChromosomeFactory<Integer, IntegerChromosome> {

    @Override
    public IntegerChromosome create(Integer[] genes) {
        return new IntegerChromosome(genes);
    }

    @Override
    public List<IntegerChromosome> createPopulation(int populationSize, int geneLength) {

        Random rnd = new Random();
        List<IntegerChromosome> initialPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(100); //  [0,99]
            }
            initialPopulation.add(this.create(genes));
        }
        return initialPopulation;
    }

}