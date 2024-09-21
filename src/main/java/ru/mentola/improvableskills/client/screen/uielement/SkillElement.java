package ru.mentola.improvableskills.client.screen.uielement;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;

@Setter @Getter
public final class SkillElement implements Renderable {
    private final Skill skill;
    private double x, y;
    private double width, height;

    private boolean selected = false;
    private boolean attached = false;
    private boolean hovered = false;
    private float alphaAnimation = 0;

    public SkillElement(Skill skill, double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skill = skill;
        this.alphaAnimation = 100;
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = Util.pointedTo(this.x, this.y, this.width, this.height, mouseX, mouseY);
        this.alphaAnimation = Util.smooth(this.alphaAnimation, (this.hovered || this.attached || this.selected) ? 250f : 100f, 3f);
        Color backgroundColor = new Color(40, 40, 40, (int) alphaAnimation);
        Color borderColor = new Color(50, 50, 50, (int) alphaAnimation);
        RenderUtil.renderRectangle(context, x, y, width, height, backgroundColor);
        RenderUtil.renderBorder(context, x, y, width, height, 1.0, borderColor);
        int textureWidth = 16;
        int textureHeight = 16;
        int textureX = (int) (this.x + (this.width - textureWidth) / 2);
        int textureY = (int) (this.y + (this.height - textureHeight) / 2);
        RenderUtil.drawTexture(
                context,
                this.skill.getTex(),
                (float) textureX,
                (float) textureY,
                0, 0,
                (float) textureWidth,
                (float) textureHeight,
                new Color(255, 255, 255, (int) alphaAnimation)
        );
    }
}