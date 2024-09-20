package ru.mentola.improvableskills.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

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

    public void drawTexture(DrawContext context, Identifier texture, float x, float y, float u, float v, float width, float height, Color color) {
        drawTexture(context, texture, x, y, 0, (float)u, (float)v, width, height, width, height, color);
    }

    private void drawTexture(DrawContext context, Identifier texture, float x, float y, float z, float u, float v, float width, float height, float textureWidth, float textureHeight, Color color) {
        drawTexture(context, texture, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight, color);
    }

    private void drawTexture(DrawContext context, Identifier texture, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight, Color color) {
        drawTexture(context, texture, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, color);
    }

    private void drawTexture(DrawContext context, Identifier texture, float x1, float x2, float y1, float y2, float z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight, Color color) {
        drawTexturedQuad(context, texture, x1, x2, y1, y2, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, color);
    }

    private void drawTexturedQuad(DrawContext context, Identifier texture, float x1, float x2, float y1, float y2, float z, float u1, float u2, float v1, float v2, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, color.getAlpha() / 255.0f);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1);
        bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2);
        bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }
}