package softcomputing.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import softcomputing.tests.fitness.MaxSumFitness;
import softcomputing.genetic.chromosome.BinaryChromosome;
import softcomputing.genetic.chromosome.Factories.BinaryChromosomeFactory;
import softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import softcomputing.genetic.chromosome.Factories.IntegerChromosomeFactory;
import softcomputing.genetic.chromosome.FloatingPointChromosome;
import softcomputing.genetic.chromosome.IntegerChromosome;
import softcomputing.genetic.core.GeneticAlgorithm;
import softcomputing.genetic.operators.crossover.SinglePointCrossover;
import softcomputing.genetic.operators.crossover.TwoPointCrossover;
import softcomputing.genetic.operators.crossover.UniformCrossover;
import softcomputing.genetic.operators.mutation.BinaryMutation;
import softcomputing.genetic.operators.mutation.IntegerMutation;
import softcomputing.genetic.operators.mutation.NonUniformMutation;
import softcomputing.genetic.operators.mutation.UniformMutation;
import softcomputing.genetic.operators.replacement.ElitismReplacement;
import softcomputing.genetic.operators.replacement.FullGenerationReplacement;
import softcomputing.genetic.operators.replacement.SteadyStateReplacement;
import softcomputing.genetic.operators.selection.RandomSelection;
import softcomputing.genetic.operators.selection.RankSelection;
import softcomputing.genetic.operators.selection.RouletteWheelSelection;
import softcomputing.genetic.utils.PopulationInitializer;
import softcomputing.tests.fitness.DecFromBinFitness;
import softcomputing.tests.fitness.MaxProductFitness;

public class TestCases {

        private static final AppLogger _logger = AppLogger.getLogger(TestCases.class);

        public static void runBinaryExample() {
                _logger.info("--- Binary Chromosome GA ---");

                int populationSize = 50;
                int geneLength = 10;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 100;

                _logger.info(String.format("Config: popSize=%d, geneLength=%d, mutationRate=%.3f, maxGen=%d",
                                populationSize, geneLength, mutationRate, maxGenerations));

                List<BinaryChromosome> population = new ArrayList<>();
                for (int i = 0; i < populationSize; i++) {
                        population.add(BinaryChromosome.random(geneLength));
                }

                ChromosomeFactory<Integer, BinaryChromosome> factory = new BinaryChromosomeFactory();
                FitnessFunction fitnessFunction = new DeciFromBinFitnessFunc();

                GeneticAlgorithm<BinaryChromosome> ga = GeneticAlgorithm.<BinaryChromosome>builder()
                                .withPopulationSize(populationSize)
                                .withPopulation(population)
                                .withChromosomeFactory(factory)
                                .withSelectionStrategy(new RouletteWheelSelection<>(population))
                                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
                                .withMutationStrategy(new BinaryMutation(mutationRate))
                                .withReplacementStrategy(new FullGenerationReplacement<>())
                                .withFitnessFunction(fitnessFunction)
                                .withMaxGenerations(maxGenerations).build();

                ga.run();
                _logger.info("Binary GA completed.\n");
        }

        public static void runIntegerExample() {
                _logger.info("--- Integer Chromosome GA ---");

                int populationSize = 50;
                int geneLength = 10;
                int geneMin = 0;
                int geneMax = 99;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 100;

                _logger.info(String.format(
                                "Config: popSize=%d, geneLength=%d, geneRange=[%d,%d], mutationRate=%.3f, maxGen=%d",
                                populationSize, geneLength, geneMin, geneMax, mutationRate, maxGenerations));

                Random rnd = new Random();
                ChromosomeFactory<Integer, IntegerChromosome> factory = new IntegerChromosomeFactory();
                // FitnessFunction fitnessFunction = new SumFitnessFun();
                List<IntegerChromosome> population = new ArrayList<>();

                for (int i = 0; i < populationSize; i++) {
                        Integer[] genes = new Integer[geneLength];
                        for (int g = 0; g < geneLength; g++) {
                                genes[g] = rnd.nextInt(geneMax - geneMin + 1) + geneMin;
                        }
                        population.add(factory.create(genes));
                }
                FitnessFunction<IntegerChromosome> fitnessFunction = new MaxSumFitness();

                GeneticAlgorithm<IntegerChromosome> ga = GeneticAlgorithm.<IntegerChromosome>builder()
                                .withPopulationSize(populationSize).withPopulation(population)
                                .withChromosomeFactory(factory).withSelectionStrategy(new RandomSelection<>())
                                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
                                .withMutationStrategy(new IntegerMutation(mutationRate))
                                .withReplacementStrategy(new FullGenerationReplacement<>())
                                .withFitnessFunction(fitnessFunction)
                                .withMaxGenerations(maxGenerations).build();

                ga.run();
                _logger.info("Integer GA completed.\n");
        }

        public static void runFloatingPointExample() {
                _logger.info("--- FloatingPoint Chromosome GA ---");

                int populationSize = 50;
                int geneLength = 10;
                double lowerBound = -10.0;
                double upperBound = 10.0;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 100;

                _logger.info(String.format(
                                "Config: popSize=%d, geneLength=%d, bounds=[%.1f,%.1f], mutationRate=%.3f, maxGen=%d",
                                populationSize, geneLength, lowerBound, upperBound, mutationRate, maxGenerations));

                List<FloatingPointChromosome> population;
                population = PopulationInitializer.randomFloatingPopulation(populationSize, geneLength, lowerBound,
                                upperBound);

                ChromosomeFactory<Double, FloatingPointChromosome> factory = new FloatingPointChromosomeFactory(
                                lowerBound, upperBound);
                FitnessFunction fitnessFunction = new ProdFitnessFun();

                GeneticAlgorithm<FloatingPointChromosome> ga = GeneticAlgorithm.<FloatingPointChromosome>builder()
                                .withPopulationSize(populationSize).withPopulation(population)
                                .withChromosomeFactory(factory).withSelectionStrategy(new RandomSelection<>())
                                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
                                .withMutationStrategy(new UniformMutation(mutationRate))
                                .withReplacementStrategy(new FullGenerationReplacement<>())
                                .withFitnessFunction(fitnessFunction)
                                .withMaxGenerations(maxGenerations).build();

                ga.run();
                _logger.info("FloatingPoint GA completed.\n");
        }

        public static void runInteger_SinglePCrsv_IntMut_FlReplacement() {

                int populationSize = 50;
                int geneLength = 10;
                int geneMin = 0;
                int geneMax = 99;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 100;

                _logger.info(String.format(
                                "Config: popSize=%d, geneLength=%d, geneRange=[%d,%d], mutationRate=%.3f, maxGen=%d",
                                populationSize, geneLength, geneMin, geneMax, mutationRate, maxGenerations));

                Random rnd = new Random();
                ChromosomeFactory<Integer, IntegerChromosome> factory = new IntegerChromosomeFactory();

                List<IntegerChromosome> intPop = PopulationInitializer.randomIntegerPopulation(populationSize,
                                geneLength, geneMin, geneMax);
                FitnessFunction<IntegerChromosome> fitnessFunction = new MaxSumFitness();

                // FitnessFunction fitnessFunction = new SumFitnessFun();

                GeneticAlgorithm<IntegerChromosome> intGa = GeneticAlgorithm.<IntegerChromosome>builder()
                                .withPopulationSize(populationSize).withPopulation(intPop)
                                .withChromosomeFactory(factory).withSelectionStrategy(new RandomSelection<>())
                                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
                                .withMutationStrategy(new IntegerMutation(mutationRate))
                                .withReplacementStrategy(new FullGenerationReplacement<>())
                                .withFitnessFunction(fitnessFunction)
                                .withMaxGenerations(maxGenerations).build();

                _logger.info("Running Integer GA  (SinglePoint + IntegerMutation + FullGeneration)");
                intGa.run();
        }

        public static void runBin_UniformCrsv_BinMut_SSReplacement() {

                int populationSize = 5;
                int geneLength = 10;
                int replacementCount = 5;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 2;
                _logger.info(String.format("Config: popSize=%d, geneLength=%d, maxGen=%d", populationSize, geneLength,
                                maxGenerations));

                ChromosomeFactory<Integer, BinaryChromosome> binFactory = new BinaryChromosomeFactory();
                List<BinaryChromosome> binPop = PopulationInitializer.randomBinaryPopulation(populationSize,
                                geneLength);

                FitnessFunction fitnessFunction = new DeciFromBinFitnessFunc();

                GeneticAlgorithm<BinaryChromosome> binGa = GeneticAlgorithm.<BinaryChromosome>builder()
                                .withPopulationSize(populationSize).withChromosomeFactory(binFactory)
                                .withPopulation(binPop).withSelectionStrategy(new RandomSelection<>())
                                .withCrossoverStrategy(new UniformCrossover<>(crossoverRate, binFactory, crossoverRate))
                                .withMutationStrategy(new BinaryMutation(mutationRate))
                                .withReplacementStrategy(new SteadyStateReplacement<>(replacementCount))
                                .withFitnessFunction(fitnessFunction)
                                .withMaxGenerations(maxGenerations).build();

                _logger.info("Running Binary GA permutation (UniformCrossover + BinaryMutation + SteadyState)");
                binGa.run();
        }

        public static void runFloating_RnkSele_2CrsOv_ElitRep() {

                int populationSize = 50;
                int geneLength = 10;
                double lowerBound = -10.0;
                double upperBound = 10.0;
                double crossoverRate = 0.7;
                double mutationRate = 0.05;
                int maxGenerations = 100;

                _logger.info(String.format(
                                "Config: popSize=%d, geneLength=%d, bounds=[%.1f,%.1f], mutationRate=%.3f, maxGen=%d",
                                populationSize, geneLength, lowerBound, upperBound, mutationRate, maxGenerations));

                List<FloatingPointChromosome> population = PopulationInitializer
                                .randomFloatingPopulation(populationSize, geneLength, lowerBound, upperBound);
                ChromosomeFactory<Double, FloatingPointChromosome> factory = new FloatingPointChromosomeFactory(
                                lowerBound, upperBound);
                FitnessFunction fitnessFunction = new ProdFitnessFun();

                GeneticAlgorithm<FloatingPointChromosome> floatGa = GeneticAlgorithm.<FloatingPointChromosome>builder()
                                .withPopulationSize(populationSize).withPopulation(population)
                                .withChromosomeFactory(factory).withSelectionStrategy(new RankSelection<>())
                                .withCrossoverStrategy(new TwoPointCrossover<>(crossoverRate, factory))
                                .withMutationStrategy(new NonUniformMutation(mutationRate, maxGenerations, 2.0))
                                .withReplacementStrategy(new ElitismReplacement<>()).withMaxGenerations(maxGenerations)
                                .withFitnessFunction(fitnessFunction)
                                .build();

                _logger.info("Running FloatingPoint GA permutation (RankSelection + TwoPointCrossover + NonUniformMutation + Elitism)");
                floatGa.run();
        }
}
