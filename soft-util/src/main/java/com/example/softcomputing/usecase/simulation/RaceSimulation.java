package com.example.softcomputing.usecase.simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.operators.crossover.UniformCrossover;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.TournametSelection;
import com.example.softcomputing.neuralnetwork.core.NeuralNetwork;
import com.example.softcomputing.usecase.simulation.entity.Car;
import com.example.softcomputing.usecase.simulation.utils.SimulationCanvas;
import com.example.softcomputing.usecase.simulation.utils.Track;

public class RaceSimulation extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final String WEIGHTS_FILE = "best_weights.dat";
    private static final boolean TRAINING_MODE = true;

    private final GeneticAlgorithm geneticAlgorithm;
    private final SimulationCanvas canvas;
    private Timer updateTimer;
    private boolean[][] trackGrid;

    public RaceSimulation() {
        // Initialize track
        Track t = new Track();
        trackGrid = t.createHShapedTrack(WIDTH, HEIGHT);
        FloatingPointChromosomeFactory factory = new FloatingPointChromosomeFactory(-1.0, 1.0);

        // set up genetic algorithm with operators
        geneticAlgorithm = new GeneticAlgorithm(
                30, // population size
                trackGrid,
                new UniformCrossover<>(factory),
                new UniformMutation(0.05),
                new TournametSelection<>(7),
                new ElitismReplacement<>());

        // Initialize simulation canvas
        canvas = new SimulationCanvas(geneticAlgorithm);

        setTitle("Neuroevolution Racing - " + (TRAINING_MODE ? "TRAINING MODE" : "INFERENCE MODE"));
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(canvas);
        setLocationRelativeTo(null);

        if (TRAINING_MODE) {
            System.out.println("Starting in TRAINING mode - Evolution enabled");
            geneticAlgorithm.initializePopulation();
        } else {
            System.out.println("Starting in INFERENCE mode - Loading saved weights");
            loadAndRunBestWeights();
        }

        startSimulation();
    }

    // Load saved weights and create a single car with those weights
    private void loadAndRunBestWeights() {
        try {
            double[] weights = loadWeights(WEIGHTS_FILE);
            if (weights == null) {
                System.err.println("No saved weights found! Switching to training mode.");
                geneticAlgorithm.initializePopulation();
                return;
            }

            // Create a single car with the loaded weights
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            nn.setWeights(weights);

            double[] startPos = { 150, 700 };
            Car car = new Car(startPos[0], startPos[1], 0, trackGrid, nn);
            car.setBest(true);

            List<Car> population = new ArrayList<>();
            population.add(car);

            geneticAlgorithm.getPopulation().clear();
            geneticAlgorithm.getPopulation().add(car);

            System.out.println("Loaded best weights with " + weights.length + " parameters");
        } catch (Exception e) {
            System.err.println("Error loading weights: " + e.getMessage());
            geneticAlgorithm.initializePopulation();
        }
    }

    // start the simulation loop
    @SuppressWarnings("unused")
    private void startSimulation() {
        updateTimer = new Timer(16, e -> {
            geneticAlgorithm.updatePopulation();

            if (TRAINING_MODE && geneticAlgorithm.shouldEvolve()) {
                // Save best weights before evolving
                Car bestCar = geneticAlgorithm.getBestCar();
                if (bestCar != null) {
                    double[] weights = bestCar.getNeuralNetwork().flatten();
                    saveWeights(weights, WEIGHTS_FILE);
                    System.out.println("Saved best weights - Fitness: " + bestCar.getFitness());
                }

                geneticAlgorithm.evolveGeneration();
            } else if (!TRAINING_MODE) {
                List<Car> population = geneticAlgorithm.getPopulation();
                if (!population.isEmpty() && !population.get(0).isAlive()) {
                    System.out.println("Car died. Restarting...");
                    loadAndRunBestWeights();
                }
            }

            canvas.repaint();
        });
        updateTimer.start();
    }

    // Save weights to file
    private void saveWeights(double[] weights, String filename) {
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(filename))) {
            dos.writeInt(weights.length);
            for (double weight : weights) {
                dos.writeDouble(weight);
            }
        } catch (IOException e) {
            System.err.println("Error saving weights: " + e.getMessage());
        }
    }

    // Load weights from file
    private double[] loadWeights(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        try (DataInputStream dis = new DataInputStream(
                new FileInputStream(filename))) {
            int length = dis.readInt();
            double[] weights = new double[length];
            for (int i = 0; i < length; i++) {
                weights[i] = dis.readDouble();
            }
            return weights;
        } catch (IOException e) {
            System.err.println("Error loading weights: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RaceSimulation simulation = new RaceSimulation();
            simulation.setVisible(true);
        });
    }
}