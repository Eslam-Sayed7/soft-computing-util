package com.example.softcomputing.usecase.simulation.utils;

public class Track {

    public boolean[][] createHShapedTrack(int WIDTH, int HEIGHT) {
        int gridWidth = WIDTH / 5;
        int gridHeight = HEIGHT / 5;
        boolean[][] track = new boolean[gridHeight][gridWidth];

        int margin = 10;
        int wallThickness = 6;
        int horizontalBarY = gridHeight / 2;

        // Outer boundaries
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < margin; y++)
                track[y][x] = true; // Top
            for (int y = gridHeight - margin; y < gridHeight; y++)
                track[y][x] = true; // Bottom
        }
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < margin; x++)
                track[y][x] = true; // Left
            for (int x = gridWidth - margin; x < gridWidth; x++)
                track[y][x] = true; // Right
        }

        // H vertical bars (inner walls)
        int leftBarX = margin + 30;
        int rightBarX = gridWidth - margin - 30 - wallThickness;

        for (int y = margin; y < horizontalBarY - 20; y++) {
            for (int x = leftBarX; x < leftBarX + wallThickness; x++)
                track[y][x] = true;
            for (int x = rightBarX; x < rightBarX + wallThickness; x++)
                track[y][x] = true;
        }
        for (int y = horizontalBarY + 20; y < gridHeight - margin; y++) {
            for (int x = leftBarX; x < leftBarX + wallThickness; x++)
                track[y][x] = true;
            for (int x = rightBarX; x < rightBarX + wallThickness; x++)
                track[y][x] = true;
        }

        // H horizontal bar walls (top and bottom of middle bar)
        for (int x = leftBarX + wallThickness; x < rightBarX; x++) {
            for (int y = horizontalBarY - 20; y < horizontalBarY - 20 + wallThickness; y++) {
                track[y][x] = true;
            }
            for (int y = horizontalBarY + 20 - wallThickness; y < horizontalBarY + 20; y++) {
                track[y][x] = true;
            }
        }

        return track;
    }
}
