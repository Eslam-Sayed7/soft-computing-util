/*
 * SimulationCanvas.java
 * responsible for rendering the simulation state
 * and visualizing the cars and track
 */
package com.example.softcomputing.usecase.simulation.utils;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.JPanel;

import com.example.softcomputing.usecase.simulation.GeneticAlgorithm;
import com.example.softcomputing.usecase.simulation.entity.Car;

public class SimulationCanvas extends JPanel {
    public static final int CELL_SIZE = 5;
    private final GeneticAlgorithm geneticAlgorithm;

    private final LinkedList<Double> outputActivationHistory;
    private int updateCounter = 0;
    private static final int LOG_INTERVAL = 10;

    public SimulationCanvas(GeneticAlgorithm ga) {
        this.geneticAlgorithm = ga;
        this.outputActivationHistory = new LinkedList<>();
        setBackground(new Color(240, 240, 240));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw track
        drawTrack(g2d);

        // Update and draw cars
        if (geneticAlgorithm.getPopulation() != null) {
            Car bestCar = geneticAlgorithm.getBestCar();
            if (bestCar != null && bestCar.isAlive()) {
                double currentOutput = bestCar.getLastOutputActivation();
                outputActivationHistory.addLast(currentOutput);
                updateCounter++;
            }

            for (Car car : geneticAlgorithm.getPopulation()) {
                car.setBest(car == bestCar);
                car.draw(g2d);
            }
            if (bestCar != null) {
                double currentOutput = bestCar.getLastOutputActivation();
                int x = (int) bestCar.getX();
                int y = (int) bestCar.getY();

                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.format("%.3f", currentOutput), x - 15, y - 23);
            }
        }

        drawStats(g2d);
    }

    private void drawTrack(Graphics2D g2d) {
        boolean[][] trackGrid = geneticAlgorithm.getTrackGrid();
        g2d.setColor(new Color(60, 60, 60));

        for (int y = 0; y < trackGrid.length; y++) {
            for (int x = 0; x < trackGrid[0].length; x++) {
                if (trackGrid[y][x]) {
                    g2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawStats(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        g2d.drawString("Generation: " + geneticAlgorithm.getGeneration(), 10, 25);
        g2d.drawString("Best Fitness: " + (int) geneticAlgorithm.getBestFitness(), 10, 50);
        g2d.drawString("Alive: " + geneticAlgorithm.getAliveCars(), 10, 75);
    }

    // Method to clear history when generation changes
    public void clearHistory() {
        outputActivationHistory.clear();
        updateCounter = 0;
        System.out.println("=== New Generation Started ===");
    }
}