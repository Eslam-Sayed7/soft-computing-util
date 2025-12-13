package softcomputing.genetic.chromosome.Factories;

import java.util.ArrayList;
import java.util.List;

import softcomputing.genetic.chromosome.BinaryChromosome;

public class BinaryChromosomeFactory implements ChromosomeFactory<Integer, BinaryChromosome> {

    @Override
    public BinaryChromosome create(Integer[] genes) {
        int[] bits = new int[genes.length];
        for (int i = 0; i < genes.length; i++) {
            bits[i] = genes[i];
        }
        return new BinaryChromosome(bits);
    }

    @Override
    public List<BinaryChromosome> createPopulation(int populationSize, int geneLength) {

        List<BinaryChromosome> initialPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int j = 0; j < geneLength; j++) {
                genes[j] = Math.random() < 0.5 ? 0 : 1;
            }
            initialPopulation.add(this.create(genes));
        }
        return initialPopulation;
    }
}
