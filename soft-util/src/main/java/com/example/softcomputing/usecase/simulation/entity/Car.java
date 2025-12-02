/*
 * Car.java
 * Entity class representing a car in the simulation.
 * Responsible for car movement, sensor updates, collision detection, and fitness calculation.
 */

package com.example.softcomputing.usecase.simulation.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.example.softcomputing.neuralnetwork.core.NeuralNetwork;
import com.example.softcomputing.usecase.simulation.utils.SimulationCanvas;

public class Car {
    // car state
    private double x, y;
    private double angle;
    private double speed;

    private static final int CAR_WIDTH = 40;
    private static final int CAR_HEIGHT = 20;
    private static final int NUM_SENSORS = 5;
    private static final double SENSOR_LENGTH = 150.0;

    // Finish line coordinates for Fitness calculation
    private static final double FINISH_X = 1100.0;
    private static final double FINISH_Y = 100.0;
    // Additional fitness parameters
    private int timeSurvived;
    private double distanceTraveled;
    // Flag to indicate if this car is the best in the population
    private boolean best = false;
    // Sensor data
    private double[] sensorDistances;
    private double[] sensorAngles;
    // neural network controlling the car
    private NeuralNetwork nn;
    // car status
    private boolean alive;
    private double fitness;
    private double closestDistanceToFinish;
    // reference to the track grid
    private boolean[][] trackGrid;
    // grid dimensions
    private final int gridWidth;
    private final int gridHeight;
    private double lastOutputActivation = 0.0;

    public Car(double startX, double startY, double startAngle, boolean[][] trackGrid) {
        this.x = startX;
        this.y = startY;
        this.angle = startAngle;
        this.speed = 2.0;
        this.trackGrid = trackGrid;
        this.gridWidth = trackGrid[0].length;
        this.gridHeight = trackGrid.length;

        // Initialize neural network
        this.nn = new NeuralNetwork(NUM_SENSORS, 8, 1);
        this.sensorDistances = new double[NUM_SENSORS];
        this.sensorAngles = new double[NUM_SENSORS];
        setupSensors();
        updateSensors();

        this.alive = true;
        this.fitness = 0;
        this.timeSurvived = 0;
        this.distanceTraveled = 0;

        // Calculate initial distance to finish
        this.closestDistanceToFinish = calculateDistanceToFinish();
    }

    public Car(double startX, double startY, double startAngle,
            boolean[][] trackGrid, NeuralNetwork nn) {
        this(startX, startY, startAngle, trackGrid);
        this.nn = new NeuralNetwork(nn);
    }

    // Setup sensor angles at angles
    private void setupSensors() {
        double[] angles = { -Math.PI / 2, -Math.PI / 4, 0, Math.PI / 4, Math.PI / 2 };
        for (int i = 0; i < NUM_SENSORS; i++) {
            sensorAngles[i] = angles[i];
        }
    }

    // for debugging
    public double getLastOutputActivation() {
        return lastOutputActivation;
    }

    // update car state
    public void update() {
        if (!alive)
            return;

        timeSurvived++;

        // Store old position for distance calculation (might not use it thu)
        double oldX = x;
        double oldY = y;

        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;

        // Calculate distance traveled
        double dx = x - oldX;
        double dy = y - oldY;
        distanceTraveled += Math.sqrt(dx * dx + dy * dy);
        // update sensor to get new readings for the neural network
        updateSensors();
        // get normalized sensor inputs
        double[] sensorInputs = normalizeSensorDistances();
        double steering = 0.0;

        try {
            // Feed sensor inputs to neural network to get steering output
            double[] outputs = nn.forward(sensorInputs);
            if (outputs != null && outputs.length > 0) {
                steering = outputs[0];
                // Clamp steering to [-1, 1] so we don't get infeasible solution
                steering = Math.max(-1.0, Math.min(1.0, steering));
                lastOutputActivation = steering;
            }
        } catch (Exception e) {
            steering = 0.0;
        }

        angle += steering * 0.15;
        if (checkCollision()) {
            alive = false;
        }

        updateFitness();
    }

    private void updateSensors() {
        for (int i = 0; i < NUM_SENSORS; i++) {
            double sensorAngle = angle + sensorAngles[i];
            sensorDistances[i] = castRay(x, y, sensorAngle);
        }
    }

    private double[] normalizeSensorDistances() {
        double[] normalized = new double[NUM_SENSORS];
        for (int i = 0; i < NUM_SENSORS; i++) {
            normalized[i] = sensorDistances[i] / SENSOR_LENGTH;
        }
        return normalized;
    }

    // fitness calculation based on distance to finish line
    private double calculateDistanceToFinish() {
        double dx = FINISH_X - x;
        double dy = FINISH_Y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void updateFitness() {
        double currentDistanceToFinish = calculateDistanceToFinish();

        if (currentDistanceToFinish < closestDistanceToFinish) {
            closestDistanceToFinish = currentDistanceToFinish;
        }

        double maxDistance = 1200.0;

        fitness = (maxDistance - closestDistanceToFinish);
    }

    public void setBest(boolean isBest) {
        this.best = isBest;
    }

    // for collision detection
    private double[][] getCarCorners() {
        double halfWidth = CAR_WIDTH / 2.0;
        double halfHeight = CAR_HEIGHT / 2.0;

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        // Calculate corners
        return new double[][] {
                { x + (-halfHeight * cos - halfWidth * sin), y + (-halfHeight * sin + halfWidth * cos) },
                { x + (-halfHeight * cos + halfWidth * sin), y + (-halfHeight * sin - halfWidth * cos) },
                { x + (halfHeight * cos - halfWidth * sin), y + (halfHeight * sin + halfWidth * cos) },
                { x + (halfHeight * cos + halfWidth * sin), y + (halfHeight * sin - halfWidth * cos) }
        };
    }

    // Cast a ray from (startX, startY) at angle rayAngle to detect distance to
    // nearest wall
    private double castRay(double startX, double startY, double rayAngle) {
        double rayX = startX;
        double rayY = startY;
        double dx = Math.cos(rayAngle);
        double dy = Math.sin(rayAngle);
        double distance = 0;
        // Step along the ray until we hit a wall or reach max sensor length
        while (distance < SENSOR_LENGTH) {
            // Move ray forward
            rayX += dx * 2;
            rayY += dy * 2;
            distance += 2;
            // Check grid cell
            int gridX = (int) (rayX / SimulationCanvas.CELL_SIZE);
            int gridY = (int) (rayY / SimulationCanvas.CELL_SIZE);

            if (gridX < 0 || gridX >= gridWidth ||
                    gridY < 0 || gridY >= gridHeight) {
                return distance;
            }

            if (trackGrid[gridY][gridX]) {
                return distance;
            }
        }

        return SENSOR_LENGTH;
    }

    // Check if any corner of the car is colliding with a wall
    private boolean checkCollision() {
        double[][] corners = getCarCorners();

        for (double[] corner : corners) {
            int gridX = (int) (corner[0] / SimulationCanvas.CELL_SIZE);
            int gridY = (int) (corner[1] / SimulationCanvas.CELL_SIZE);

            if (gridX < 0 || gridX >= gridWidth ||
                    gridY < 0 || gridY >= gridHeight) {
                return true;
            }

            if (trackGrid[gridY][gridX]) {
                return true;
            }
        }

        return false;
    }

    public void draw(Graphics2D g2d) {
        if (!alive)
            return;

        // Draw sensors
        g2d.setColor(new Color(255, 255, 0, 100));
        for (int i = 0; i < NUM_SENSORS; i++) {
            double sensorAngle = angle + sensorAngles[i];
            double endX = x + Math.cos(sensorAngle) * sensorDistances[i];
            double endY = y + Math.sin(sensorAngle) * sensorDistances[i];
            g2d.drawLine((int) x, (int) y, (int) endX, (int) endY);
        }

        AffineTransform old = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(angle);

        if (!alive)
            g2d.setColor(Color.GRAY);
        else if (best)
            g2d.setColor(Color.GREEN);
        else
            g2d.setColor(Color.RED);
        g2d.fillRect(-CAR_HEIGHT / 2, -CAR_WIDTH / 2, CAR_HEIGHT, CAR_WIDTH);

        g2d.setTransform(old);
    }

    // Getters
    public boolean isAlive() {
        return alive;
    }

    public double getFitness() {
        return fitness;
    }

    public double getClosestDistanceToFinish() {
        return closestDistanceToFinish;
    }

    public NeuralNetwork getNeuralNetwork() {
        return nn;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public void setNeuralNetwork(NeuralNetwork brain) {
        this.nn = new NeuralNetwork(brain);
    }
}