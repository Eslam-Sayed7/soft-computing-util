package softcomputing.genetic.chromosome.Factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.FloatingPointChromosome;

public class FloatingPointChromosomeFactory implements ChromosomeFactory<Double, FloatingPointChromosome> {

    private final double lowerBound;
    private final double upperBound;
    private final Random rand = new Random();

    public FloatingPointChromosomeFactory(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public FloatingPointChromosome create(Double[] genes) {
        // Assumes genes already sized correctly
        return new FloatingPointChromosome(genes, lowerBound, upperBound);
    }

    @Override
    public List<FloatingPointChromosome> createPopulation(int populationSize, int geneLength) {
        List<FloatingPointChromosome> initialPopulation = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            Double[] genes = new Double[geneLength];
            for (int j = 0; j < geneLength; j++) {
                genes[j] = lowerBound + rand.nextDouble() * (upperBound - lowerBound);
            }
            initialPopulation.add(this.create(genes));
        }

        return initialPopulation;
    }
}
