package softcomputing.genetic.utils;

import softcomputing.genetic.chromosome.BinaryChromosome;
import softcomputing.genetic.chromosome.IntegerChromosome;
import softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import softcomputing.genetic.chromosome.FloatingPointChromosome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationInitializer {

    
    public static List<BinaryChromosome> randomBinaryPopulation(int populationSize, int geneLength) {
        List<BinaryChromosome> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(BinaryChromosome.random(geneLength));
        }
        return population;
    }

    public static List<IntegerChromosome> randomIntegerPopulation(int populationSize, int geneLength, int min, int max) {
        List<IntegerChromosome> population = new ArrayList<>(populationSize);
        Random rnd = new Random();
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(max - min + 1) + min;
            }
            population.add(new IntegerChromosome(genes));
        }
        return population;
    }

    public static List<FloatingPointChromosome> randomFloatingPopulation(int populationSize, int geneLength, double lowerBound, double upperBound) {
        List<FloatingPointChromosome> population = new ArrayList<>(populationSize);
        FloatingPointChromosomeFactory factory = new FloatingPointChromosomeFactory(lowerBound, upperBound);
        population = factory.createPopulation(populationSize, geneLength);
        return population;
    }
}
