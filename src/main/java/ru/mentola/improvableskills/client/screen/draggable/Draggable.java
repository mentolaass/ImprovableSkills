package ru.mentola.improvableskills.client.screen.draggable;

import lombok.Getter;

@Getter
public final class Draggable {
    private int offsetY = 0, offsetX = 0;
    private double lastMouseX = 0, lastMouseY = 0;
    private boolean isDragging;

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            this.isDragging = true;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging && button != 0) {
            this.offsetX += (int) (mouseX - this.lastMouseX);
            this.offsetY += (int) (mouseY - this.lastMouseY);
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isDragging && button != 0) {
            this.isDragging = false;
        }
    }
}