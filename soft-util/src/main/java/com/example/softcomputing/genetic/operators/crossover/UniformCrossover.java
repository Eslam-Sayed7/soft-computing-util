package com.example.softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;

public class UniformCrossover<T, C extends Chromosome<T>> implements CrossoverStrategy<C> {
	private static final Random rand = new Random();
	private final double mixingRatio;
	private final double crossoverRate;
	private final ChromosomeFactory<T, C> factory;

	public UniformCrossover(ChromosomeFactory<T, C> factory) {
		this(0.7, factory, 0.5);
	}

	public UniformCrossover(double crossoverRate, ChromosomeFactory<T, C> factory, double mixingRatio) {
		if (mixingRatio < 0.0 || mixingRatio > 1.0)
			throw new IllegalArgumentException("mixingRatio must be in [0,1]");
		this.mixingRatio = mixingRatio;
		this.factory = factory;
		this.crossoverRate = crossoverRate;
	}

	@Override
	public List<C> crossover(C parent1, C parent2) {
		int length = parent1.length();
		if (length != parent2.length())
			throw new IllegalArgumentException("Parents must have the same length");

		if (rand.nextDouble() > crossoverRate) {
			List<C> offspring = new ArrayList<>();
			offspring.add(parent1);
			offspring.add(parent2);
			return offspring;
		}

		T[] child1Genes = parent1.toArray();
		T[] child2Genes = parent2.toArray();

		for (int i = 0; i < length; i++) {
			double r = rand.nextDouble();
			if (r < mixingRatio) {
				child1Genes[i] = parent2.getGene(i);
				child2Genes[i] = parent1.getGene(i);
			} else {
				child1Genes[i] = parent1.getGene(i);
				child2Genes[i] = parent2.getGene(i);
			}
		}
		List<C> offspring = new ArrayList<>();
		offspring.add(factory.create(child1Genes));
		offspring.add(factory.create(child2Genes));
		return offspring;
	}
}
