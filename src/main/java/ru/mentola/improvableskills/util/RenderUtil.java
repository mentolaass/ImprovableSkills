package ru.mentola.improvableskills.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

@UtilityClass
public class RenderUtil {
    public void renderRectangle(DrawContext context, double x, double y, double width, double height, Color backgroundColor) {
        context.fill((int) x, (int) y,(int)(x + width), (int)(y + height), backgroundColor.getRGB());
    }

    public void renderBorder(DrawContext context, double x, double y, double width, double height, double thickness, Color borderColor) {
        context.fill((int) x, (int) y, (int) (x + width), (int) (y + thickness), borderColor.getRGB());
        context.fill((int) x, (int) (y + height - thickness), (int) (x + width), (int) (y + height), borderColor.getRGB());
        context.fill((int) x, (int) (y + thickness), (int) (x + thickness), (int) (y + height - thickness), borderColor.getRGB());
        context.fill((int) (x + width - thickness), (int) (y + thickness), (int) (x + width), (int) (y + height - thickness), borderColor.getRGB());
    }
}