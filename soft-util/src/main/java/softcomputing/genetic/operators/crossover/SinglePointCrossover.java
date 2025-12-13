package softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.Chromosome;
import softcomputing.genetic.chromosome.Factories.ChromosomeFactory;

public class SinglePointCrossover<G, C extends Chromosome<G>> implements CrossoverStrategy<C> {
    private final ChromosomeFactory<G, C> _factory;
    private double _crossoverProbability = 0.7;
    private final Random _random = new Random();

    public SinglePointCrossover(double crossoverProbability, ChromosomeFactory<G, C> factory) {
        this._crossoverProbability = crossoverProbability;
        this._factory = factory;
    }

    @Override
    public List<C> crossover(C parent1, C parent2) {
        List<C> children = new ArrayList<>();
        
        int length = parent1.length();
        if (length != parent2.length()) {
            throw new IllegalArgumentException("Parents must have the same length");
        }

        Random DoCrossover = new Random();
        if (DoCrossover.nextDouble() > _crossoverProbability) {
            children.add(parent1);
            children.add(parent2);
            return children;
        }

        // random crossover point
        int crossoverPoint = 1 + _random.nextInt(length - 1);

        G[] p1Genes = parent1.toArray();
        G[] p2Genes = parent2.toArray();

        G[] child1Genes = p1Genes.clone();
        G[] child2Genes = p2Genes.clone();

        for (int i = crossoverPoint; i < length; i++) {
            child1Genes[i] = p2Genes[i];
            child2Genes[i] = p1Genes[i];
        }

        children.add(_factory.create(child1Genes));
        children.add(_factory.create(child2Genes));

        return children;
    }
}
