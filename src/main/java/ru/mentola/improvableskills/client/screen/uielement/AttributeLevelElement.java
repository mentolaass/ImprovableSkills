package ru.mentola.improvableskills.client.screen.uielement;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;

@Setter @Getter
public final class AttributeLevelElement implements Renderable {
    private final Attribute<?> attribute;
    private double x, y;
    private double width, height;

    private boolean hovered = false;
    private boolean attached = false;
    private boolean selected = false;
    private float alphaAnimation = 0;
    private int level;

    public AttributeLevelElement(Attribute<?> attribute, int level, double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.level = level;
        this.attribute = attribute;
        this.alphaAnimation = 100;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = Util.pointedTo(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.alphaAnimation = Util.smooth(this.alphaAnimation, (this.hovered || this.attached || this.selected) ? 250f : 50f, 3f);
        Color backgroundColor = new Color(40, 40, 40, (int) alphaAnimation);
        Color borderColor = new Color(50, 50, 50, (int) alphaAnimation);
        RenderUtil.renderRectangle(context, x, y, width, height, backgroundColor);
        RenderUtil.renderBorder(context, x, y, width, height, 1.0, borderColor);
        Text name = Text.of(String.valueOf(this.level));
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, name, (int) (this.x + (width / 2 - (double) textRenderer.getWidth(name) / 2)), (int) (this.y + (height / 2 - textRenderer.fontHeight / 2)), new Color(190, 190, 190, 200).getRGB(), false);
    }
}