package ru.mentola.improvableskills.client.screen.uielement;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.attribute.NumberAttribute;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;

@Setter @Getter
public final class AttributeElement implements Renderable {
    private final Attribute<?> attribute;
    private final Skill skill;
    private double x, y;
    private double width, height;

    private boolean hovered = false;
    private float alphaAnimation = 0;
    private final Text name;
    private float time = 0;

    public AttributeElement(Skill skill, Attribute<?> attribute, double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.attribute = attribute;
        this.skill = skill;
        this.alphaAnimation = 255;
        String[] nameStrArray = this.attribute.getName().getString().split(" ");
        this.name = Text.of(nameStrArray.length == 1 ? nameStrArray[0].substring(0, 1).toUpperCase() : (nameStrArray.length == 0 ? "N" : (nameStrArray[0].substring(0, 1).toUpperCase() + nameStrArray[1].substring(0, 1).toUpperCase())));
    }

    @Override
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hovered = Util.pointedTo(this.x, this.y, this.width, this.height, mouseX, mouseY);
        Color backgroundColor = new Color(40, 40, 40, (int) alphaAnimation);
        Color borderColor = new Color(50, 50, 50, (int) alphaAnimation);
        RenderUtil.renderRectangle(context, x, y, width, height, backgroundColor);
        RenderUtil.renderBorder(context, x, y, width, height, 1.0, borderColor);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, this.name, (int) (this.x + (width / 2 - (double) textRenderer.getWidth(name) / 2)), (int) (this.y + (height / 2 - textRenderer.fontHeight / 2)), new Color(190, 190, 190, 200).getRGB(), false);

        PlayerData playerData = DataProvider.get(PlayerData.class);
        if (playerData != null && playerData.getSkill(this.skill.getId()).getAttribute(this.attribute.getIdentifier()).getLevel() == Util.getMaxLevelAttribute((NumberAttribute<?>) this.attribute)) {
            double offsetY = Math.sin(this.time) * 2;
            this.time += delta * 0.2f;
            context.drawText(textRenderer, Text.of("MAX"), (int) (this.x + (this.getWidth() / 2 - (double) textRenderer.getWidth(Text.of("MAX")) / 2)), (int) (this.y - textRenderer.fontHeight + offsetY), new Color(221, 95, 246, 150).getRGB(), false);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AttributeElement element)
            return element.getAttribute().getIdentifier().equals(this.getAttribute().getIdentifier());
        return false;
    }
}