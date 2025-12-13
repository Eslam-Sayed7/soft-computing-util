package softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.Chromosome;
import softcomputing.genetic.chromosome.Factories.ChromosomeFactory;

public class TwoPointCrossover<G, C extends Chromosome<G>> implements CrossoverStrategy<C> {
    private final double crossoverProbability;
    private final ChromosomeFactory<G, C> factory;
    private final Random random = new Random();

    public TwoPointCrossover(double probability, ChromosomeFactory<G, C> factory) {
        this.crossoverProbability = probability;
        this.factory = factory;
    }

    @Override
    public List<C> crossover(C parent1, C parent2) {
        List<C> children = new ArrayList<>();

        if (random.nextDouble() < crossoverProbability) {
            children.add(parent1);
            children.add(parent2);
            return children;
        }

        int len = parent1.length();
        if (len != parent2.length()) {
            throw new IllegalArgumentException("Parents must be of the same length");
        }

        int point1 = random.nextInt(len);
        int point2 = random.nextInt(len);
        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        G[] p1Genes = parent1.toArray();
        G[] p2Genes = parent2.toArray();
        G[] child1Genes = p1Genes.clone();
        G[] child2Genes = p2Genes.clone();

        for (int i = point1; i < point2; i++) {
            G temp = child1Genes[i];
            child1Genes[i] = child2Genes[i];
            child2Genes[i] = temp;
        }

        C child1 = factory.create(child1Genes);
        C child2 = factory.create(child2Genes);

        children.add(child1);
        children.add(child2);
        return children;
    }
}
