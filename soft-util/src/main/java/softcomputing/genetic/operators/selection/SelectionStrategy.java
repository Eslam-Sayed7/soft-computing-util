package softcomputing.genetic.operators.selection;

import java.util.List;

import softcomputing.genetic.chromosome.Chromosome;

/**
 * Strategy interface for selecting individuals from a population in a genetic algorithm.
 * <p>
 * Selection is a key genetic operator that chooses chromosomes from the current population
 * to serve as parents for the next generation. Different selection strategies apply different
 * selection pressures, influencing the balance between exploration and exploitation.
 * </p>
 * 
 * <h2>Common Implementations</h2>
 * <ul>
 *   <li><b>Roulette Wheel Selection:</b> Selects individuals with probability proportional to fitness</li>
 *   <li><b>Tournament Selection:</b> Randomly selects k individuals and chooses the best</li>
 *   <li><b>Rank Selection:</b> Selects based on rank rather than raw fitness values</li>
 *   <li><b>Random Selection:</b> Selects individuals uniformly at random</li>
 * </ul>
 * 
 * @param <C> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see softcomputing.genetic.operators.selection.RouletteWheelSelection
 * @see softcomputing.genetic.operators.selection.TournametSelection
 * @see softcomputing.genetic.operators.selection.RankSelection
 */
public interface SelectionStrategy <C extends Chromosome<?>> {
	
	/**
	 * Selects a single individual from the population.
	 * 
	 * @param population the current population to select from
	 * @return the selected chromosome
	 * @throws IllegalArgumentException if population is null or empty
	 */
	C selectIndividual(List<C> population);
}
