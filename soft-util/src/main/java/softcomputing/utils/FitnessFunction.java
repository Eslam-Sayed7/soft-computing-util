package softcomputing.utils;

import softcomputing.genetic.chromosome.Chromosome;

/**
 * Functional interface for evaluating the fitness of a chromosome.
 * <p>
 * The fitness function is problem-specific and determines how well a chromosome
 * solves the optimization problem. Higher fitness values indicate better solutions.
 * </p>
 * 
 * @param <C> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 */
public interface FitnessFunction<C extends Chromosome<?>> {
    
    /**
     * Evaluates the fitness of the given chromosome.
     * 
     * @param chromosome the chromosome to evaluate
     * @return the fitness value (higher is better)
     */
    double evaluate(C chromosome);
}
