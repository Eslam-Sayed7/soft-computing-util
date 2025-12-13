package softcomputing.genetic.operators.replacement;

import java.util.List;

import softcomputing.genetic.chromosome.Chromosome;

/**
 * Strategy interface for replacing the population in a genetic algorithm.
 * <p>
 * Replacement determines how the new generation is formed from the current population
 * and newly created offspring. Different strategies balance exploration and exploitation
 * by controlling how much of the good genetic material is preserved.
 * </p>
 * 
 * <h2>Common Implementations</h2>
 * <ul>
 *   <li><b>Full Generation Replacement:</b> Completely replaces the old population with offspring</li>
 *   <li><b>Elitism Replacement:</b> Preserves the best individuals from the current generation</li>
 *   <li><b>Steady-State Replacement:</b> Replaces only a portion of the population each generation</li>
 * </ul>
 * 
 * @param <C> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see softcomputing.genetic.operators.replacement.FullGenerationReplacement
 * @see softcomputing.genetic.operators.replacement.ElitismReplacement
 * @see softcomputing.genetic.operators.replacement.SteadyStateReplacement
 */
public interface Replacement<C extends Chromosome<?>> {
	
	/**
	 * Creates a new population by combining individuals from the current population and new offspring.
	 * 
	 * @param currentPopulation the current generation's population
	 * @param newIndividuals the newly created offspring
	 * @return the population for the next generation
	 * @throws IllegalArgumentException if either list is null
	 */
	List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals);
}