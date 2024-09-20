package ru.mentola.improvableskills.client.screen.element;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.client.util.RenderUtil;

import java.awt.*;

@AllArgsConstructor @Getter
public final class SkillElement {
    private final Identifier texture;
    private final Skill skill;

    public void render(DrawContext context, int offsetX, int offsetY, boolean selected) {
        Color color = selected ? new Color(30, 30, 30, 200) : new Color(40, 40, 40, 200);
        RenderUtil.renderRectangle(context, offsetX, offsetY, 32, 32, color);
        if (selected) RenderUtil.renderBorder(context, offsetX, offsetY, 32, 32, 1, new Color(50, 50, 50));
        context.drawTexture(texture, offsetX + 4, offsetY + 4, 0, 0, 24, 24, 24, 24);
    }
}