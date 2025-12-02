/*
 * GeneticAlgorithm.java
 * implements a genetic algorithm to evolve a population of cars
 * using neural networks to navigate a track
 * runs the simulation and evolution process 
 */

package com.example.softcomputing.usecase.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;
import com.example.softcomputing.neuralnetwork.core.NeuralNetwork;
import com.example.softcomputing.usecase.simulation.entity.Car;
import com.example.softcomputing.usecase.simulation.utils.CarInfeasibleSolution;
import com.example.softcomputing.utils.InfeasibleSolution;

public class GeneticAlgorithm {
    private final int populationSize;
    private final boolean[][] trackGrid;

    private final CrossoverStrategy<FloatingPointChromosome> crossoverStrategy;
    private final MutationStrategy<FloatingPointChromosome> mutationStrategy;
    private final SelectionStrategy<FloatingPointChromosome> selectionStrategy;
    private final Replacement<FloatingPointChromosome> replacementStrategy;
    private final FloatingPointChromosomeFactory chromosomeFactory;

    private List<Car> population;
    private int generation;
    private double bestFitness;
    private Car bestCar;
    private double avgFitness;
    private int aliveCars;
    private long generationStartTime;
    private final long GENERATION_TIMEOUT_MS = 30_000;
    InfeasibleSolution<FloatingPointChromosome> infeasibleCheck = new CarInfeasibleSolution();
    private final Random random = new Random();

    public GeneticAlgorithm(
            int populationSize,
            boolean[][] trackGrid,
            CrossoverStrategy<FloatingPointChromosome> crossoverStrategy,
            MutationStrategy<FloatingPointChromosome> mutationStrategy,
            SelectionStrategy<FloatingPointChromosome> selectionStrategy,
            Replacement<FloatingPointChromosome> replacementStrategy) {
        this.populationSize = populationSize;
        this.trackGrid = trackGrid;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.selectionStrategy = selectionStrategy;
        this.replacementStrategy = replacementStrategy;
        this.chromosomeFactory = new FloatingPointChromosomeFactory(-1.0, 1.0);
        this.population = new ArrayList<>();
        this.generation = 0;
    }

    // Initialize population of cars at starting position
    public void initializePopulation() {
        population.clear();
        double[] startPos = { 150, 700 };
        // Create cars with random neural networks
        for (int i = 0; i < populationSize; i++) {
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double startAngle = random.nextDouble() * Math.PI * 2;
            Car car = new Car(startPos[0], startPos[1], startAngle, trackGrid, nn);
            population.add(car);
        }

        generationStartTime = System.currentTimeMillis();
        System.out.println("Initialized population with " + populationSize + " cars at position (" +
                (int) startPos[0] + ", " + (int) startPos[1] + ")");
    }

    public void updatePopulation() {
        aliveCars = 0;
        Car currentBest = null;
        double maxFitness = Double.NEGATIVE_INFINITY;

        for (Car car : population) {
            car.update();
            if (car.isAlive())
                aliveCars++;

            if (car.getFitness() > maxFitness) {
                maxFitness = car.getFitness();
                currentBest = car;
            }
        }

        // Store best car for this generation to visualize it
        bestCar = currentBest;
        bestFitness = maxFitness;
    }

    // determine if generation should evolve
    public boolean shouldEvolve() {
        long elapsed = System.currentTimeMillis() - generationStartTime;
        boolean timeoutReached = elapsed >= GENERATION_TIMEOUT_MS;
        boolean allDead = aliveCars == 0;
        return allDead || timeoutReached;
    }

    // evolve to next generation
    public void evolveGeneration() {
        generation++;
        // Convert cars to chromosomes
        List<FloatingPointChromosome> parents = new ArrayList<>(populationSize);
        for (int i = 0; i < population.size(); i++) {
            Car car = population.get(i);
            NeuralNetwork nn = car.getNeuralNetwork();
            double[] weights = nn.flatten();
            // we need to box the double[] to Double[] for the chromosome factory
            Double[] boxed = Arrays.stream(weights).boxed().toArray(Double[]::new);
            FloatingPointChromosome chrom = chromosomeFactory.create(boxed);
            // get fitness to assign to this chromosome
            chrom.setFitness(car.getFitness());
            parents.add(chrom);
        }
        // calculate best fitness
        OptionalDouble max = parents.stream().mapToDouble(FloatingPointChromosome::getFitness).max();
        bestFitness = max.orElse(0);
        // new population
        List<FloatingPointChromosome> newChromosomes = new ArrayList<>(populationSize);
        // create new chromosomes until we reach population size
        while (newChromosomes.size() < populationSize) {
            FloatingPointChromosome parent1 = selectionStrategy.selectIndividual(parents);
            FloatingPointChromosome parent2 = selectionStrategy.selectIndividual(parents);
            // we can log the selected parents for the first few operations
            int p1Index = parents.indexOf(parent1);
            int p2Index = parents.indexOf(parent2);
            // crossover
            List<FloatingPointChromosome> children = crossoverStrategy.crossover(parent1, parent2);
            // mutation for each child
            for (int childIdx = 0; childIdx < children.size(); childIdx++) {
                FloatingPointChromosome child = children.get(childIdx);
                FloatingPointChromosome mutated = mutationStrategy.mutate(child);
                // check infeasible
                if (infeasibleCheck.checkInfeasible(mutated)) {
                    mutated = child;
                }
                mutated.setFitness(0.0);
                newChromosomes.add(mutated);
                if (newChromosomes.size() >= populationSize)
                    break;
            }
        }
        // replacement
        List<FloatingPointChromosome> nextGenChromosomes = replacementStrategy.replacePopulation(parents,
                newChromosomes);
        // create new cars from chromosomes
        List<Car> newPopulation = new ArrayList<>(populationSize);
        double[] startPos = { 150, 700 };

        for (int i = 0; i < nextGenChromosomes.size(); i++) {
            FloatingPointChromosome chrom = nextGenChromosomes.get(i);
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double[] genes = Arrays.stream(chrom.toArray()).mapToDouble(Double::doubleValue).toArray();
            nn.setWeights(genes);

            double offsetX = (random.nextDouble() - 0.5) * 20;
            double offsetY = (random.nextDouble() - 0.5) * 20;
            double startAngle = random.nextDouble() * Math.PI * 2;

            Car car = new Car(startPos[0] + offsetX, startPos[1] + offsetY, startAngle, trackGrid, nn);
            newPopulation.add(car);

        }

        this.population = newPopulation;

        generationStartTime = System.currentTimeMillis();
    }

    // Getters
    public int getAliveCars() {
        return aliveCars;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getAvgFitness() {
        return avgFitness;
    }

    public int getGeneration() {
        return generation;
    }

    public List<Car> getPopulation() {
        return population;
    }

    public Car getBestCar() {
        return bestCar;
    }

    public boolean[][] getTrackGrid() {
        return trackGrid;
    }
}