package softcomputing.genetic.operators.selection;

import java.util.List;
import java.util.Random;
import softcomputing.genetic.chromosome.Chromosome;

public class RandomSelection<C extends Chromosome<?>> implements SelectionStrategy<C> {
    private final Random rnd = new Random();

    @Override
    public C selectIndividual(List<C> population) {
        if (population == null || population.isEmpty()) return null;
        return population.get(rnd.nextInt(population.size()));
    }
}
