package ru.mentola.improvableskills.client.screen.uielement;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;

@Getter @Setter
public final class ButtonElement implements Renderable {
    private double x, y;
    private double width, height;
    private Text text;
    private Runnable runnable;

    private boolean hovered = false;
    private boolean enabled = true;
    private float alphaAnimation = 0;

    public ButtonElement(Text content, double x, double y, double width, double height, Runnable runnable) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = content;
        this.alphaAnimation = 100;
        this.runnable = runnable;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = Util.pointedTo(this.x, this.y, this.width, this.height, mouseX, mouseY);
        alphaAnimation = isEnabled() ? Util.smooth(this.alphaAnimation, this.hovered ? 250 : 100, 3f) : 100;
        Color backgroundColor = new Color(40, 40, 40, (int) alphaAnimation);
        Color borderColor = new Color(50, 50, 50, (int) alphaAnimation);
        Color fontColor = new Color(190, 190, 190, (int) alphaAnimation);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.renderRectangle(context, x, y, width, height, backgroundColor);
        RenderUtil.renderBorder(context, x, y, width, height, 1.0, borderColor);
        int textWidth = textRenderer.getWidth(this.text);
        int textHeight = textRenderer.fontHeight;
        int textX = (int) (this.x + (this.width - textWidth) / 2);
        int textY = (int) (this.y + (this.height - textHeight) / 2);
        context.drawText(
                textRenderer,
                this.text,
                textX,
                textY,
                fontColor.getRGB(),
                false
        );
    }
}
