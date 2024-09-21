package ru.mentola.improvableskills.client.animation;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;

public final class SquareAnimation extends Animation {
    private final int numberOfSquares;
    private final int squareSize;
    private final int maxDistance;
    private final Point[] squarePositions;
    private final double[] velocitiesX;
    private final double[] velocitiesY;
    private boolean initialized;

    public SquareAnimation(Identifier identifier, int workTime, int numberOfSquares, int squareSize, int maxDistance) {
        super(identifier, workTime, System.currentTimeMillis());
        this.squareSize = squareSize;
        this.maxDistance = maxDistance;
        this.numberOfSquares = numberOfSquares;
        squarePositions = new Point[numberOfSquares];
        velocitiesY = new double[numberOfSquares];
        velocitiesX = new double[numberOfSquares];
        this.initialized = false;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY) {
        if (!this.initialized) {
            for (int i = 0; i < numberOfSquares; i++) {
                squarePositions[i] = new Point((int) mouseX, (int) mouseY);
                velocitiesX[i] = (Math.random() * 2 - 1) * Util.randomNumber(maxDistance, 0) / this.getWorkTime();
                velocitiesY[i] = (Math.random() * 2 - 1) * Util.randomNumber(maxDistance, 0) / this.getWorkTime();
            }
            this.initialized = true;
        }
        long elapsedTime = System.currentTimeMillis() - this.getStartTime();
        if (!isDie()) {
            for (int i = 0; i < numberOfSquares; i++) {
                int x = (int) (squarePositions[i].x + velocitiesX[i] * elapsedTime);
                int y = (int) (squarePositions[i].y + velocitiesY[i] * elapsedTime);
                int alpha_ = 255 - (int) (255 * ((double) elapsedTime / this.getWorkTime()));
                RenderUtil.renderRectangle(context, x, y, squareSize, squareSize, new Color(255, 255, 255, alpha_));
            }
        }
    }
}