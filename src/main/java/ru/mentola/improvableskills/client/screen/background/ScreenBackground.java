package ru.mentola.improvableskills.client.screen.background;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import ru.mentola.improvableskills.client.util.RenderUtil;

import java.awt.*;

@RequiredArgsConstructor
public final class ScreenBackground {
    private final int offsetX, offsetY;

    private int horizontalAnimationPosition;
    private long horizontalAnimationUpdateTime;

    public void render(DrawContext context) {
        double scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        double scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();

        int currentPosition = 0;
        for (int y = 0; y < scaledHeight; y+=offsetY) {
            boolean scale = horizontalAnimationPosition == currentPosition;
            if (System.currentTimeMillis() - horizontalAnimationUpdateTime >= 100) {
                horizontalAnimationPosition++;
                horizontalAnimationUpdateTime = System.currentTimeMillis();
            }
            for (int x = 0; x < scaledWidth; x+=offsetX) {
                RenderUtil.renderRectangle(context, x, y, scale ? 2 : 1, scale ? 2 : 1, new Color(128, 128, 128, 128));
            }
            currentPosition++;
        }
        if (horizontalAnimationPosition >= scaledHeight / 30)
            horizontalAnimationPosition = 0;
    }
}