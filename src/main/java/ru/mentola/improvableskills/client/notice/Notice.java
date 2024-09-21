package ru.mentola.improvableskills.client.notice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.client.util.RenderUtil;

import java.awt.*;

@RequiredArgsConstructor @Getter
public final class Notice {
    private final String text;
    private final Identifier textureId;
    private final long startTime = System.currentTimeMillis();
    private final int displayDuration = 1000;

    private long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public void render(DrawContext context) {
        long elapsedTime = getElapsedTime();
        if (elapsedTime > displayDuration) {
            return;
        }
        float progress = elapsedTime / (float) displayDuration;
        if (progress >= 0.98f) return;
        int yPos = (int) (10 + progress * 20);
        int alpha = (int) (255 * (1 - progress));
        int color = (alpha << 24) | 0xFFFFFF;
        context.drawText(MinecraftClient.getInstance().textRenderer, text, 25, yPos, color, false);
        RenderUtil.drawTexture(context, textureId, 10, yPos - 1.5f, 0, 0, 10, 10, new Color(255, 255, 255, alpha));
    }

    public boolean isAlive() {
        return getElapsedTime() <= displayDuration;
    }
}