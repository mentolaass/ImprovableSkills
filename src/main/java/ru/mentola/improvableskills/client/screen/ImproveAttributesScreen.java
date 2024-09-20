package ru.mentola.improvableskills.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.PumpSkillAttributePayload;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.attribute.NumberAttribute;
import ru.mentola.improvableskills.client.sound.CustomSound;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;
import java.util.Set;

public final class ImproveAttributesScreen extends Screen {
    private final Screen parent;
    private final Skill skill;

    private long horizontalAnimationLastUpdateTime = 0;
    private int horizontalAnimationPosition = 0;

    private final Set<Attribute<?>> tempAttributes;
    private boolean attributesChanged = false;
    private int pointsToUpgrade = 0;

    public ImproveAttributesScreen(Screen parent, Skill skill) {
        super(Text.of("Attributes Improve Screen"));

        this.parent = parent;
        this.skill = skill;
        this.tempAttributes = Util.copyAttributes(skill.getAttributes());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        double scaledHeight = MinecraftClient.getInstance()
                .getWindow()
                .getScaledHeight();
        double scaledWidth = MinecraftClient.getInstance()
                .getWindow()
                .getScaledWidth();

        PlayerData playerData = DataProvider.get(PlayerData.class);
        if (playerData != null) {
            this.renderPoints(context, scaledHeight, scaledWidth);
            this.renderDialog(context, scaledWidth, scaledHeight);
            this.renderLevelAndPointsInfo(context, playerData);
        }
    }

    private void renderDialog(DrawContext context, double scaledWidth, double scaledHeight) {
        double height = (125 + this.skill.getAttributes().size() * 10);
        double x = scaledWidth / 2 - 200 / 2;
        double y = scaledHeight / 2 - height / 2;
        RenderUtil.renderRectangle(context, x, y, 200, height, new Color(40, 40, 40, 200));
        RenderUtil.renderRectangle(context, x, y, 200, 20, new Color(30, 30, 30, 200));
        context.drawText(MinecraftClient.getInstance().textRenderer, Constants.UPGRADE_ATTRIBUTES, (int) (x + (double) 200 / 2) - textRenderer.getWidth("Улучшение атрибутов") / 2, (int) (y + 7), new Color(190, 190, 190).getRGB(), false);
        RenderUtil.renderBorder(context, x, y, 200, height, 1, new Color(50, 50, 50));
        int textY = 10;
        for (Attribute<?> tempAttribute : tempAttributes) {
            Attribute<?> skillAttribute = skill.getAttribute(tempAttribute.getIdentifier());
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    Text.of(tempAttribute.getName().getString() + " +" + ((int) tempAttribute.getLevel() - skillAttribute.getLevel())),
                    (int) (x + 10),
                    (int) (y + 20 + textY),
                    new Color(190, 190, 190).getRGB(),
                    false);
            // btns
            RenderUtil.renderRectangle(context, x + 200 - 25, y + 20 + textY - 5, 15, 15, new Color(30, 30, 30, 200));
            RenderUtil.renderBorder(context, x + 200 - 25, y + 20 + textY - 5, 15, 15, 1, new Color(50, 50, 50, 200));
            context.drawText(MinecraftClient.getInstance().textRenderer, Text.of("+"), (int) (x + 200 - 25 + (double) 8) - textRenderer.getWidth(Text.of("+")) / 2, (int) (y + 20 + textY - 1), new Color(190, 190, 190, 200).getRGB(), false);

            RenderUtil.renderRectangle(context, x + 200 - 45, y + 20 + textY - 5, 15, 15, new Color(30, 30, 30, 200));
            RenderUtil.renderBorder(context, x + 200 - 45, y + 20 + textY - 5, 15, 15, 1, new Color(50, 50, 50, 200));
            context.drawText(MinecraftClient.getInstance().textRenderer, Text.of("-"), (int) (x + 200 - 45 + (double) 8) - textRenderer.getWidth(Text.of("-")) / 2, (int) (y + 20 + textY - 1), new Color(190, 190, 190, 200).getRGB(), false);

            textY += MinecraftClient.getInstance().textRenderer.fontHeight + 7;
        }
        PlayerData playerData = DataProvider.get(PlayerData.class);
        if (playerData != null) {
            int alpha = attributesChanged ? 200 : 50;
            Text textAcceptBtn = Constants.APPLY;
            RenderUtil.renderRectangle(context, x + 10, y + height - 30, 180, 20, new Color(30, 30, 30, alpha));
            RenderUtil.renderBorder(context, x + 10, y + height - 30, 180, 20, 1, new Color(50, 50, 50, alpha));
            context.drawText(MinecraftClient.getInstance().textRenderer, textAcceptBtn, (int) (x + 10 + (double) 180 / 2) - textRenderer.getWidth(textAcceptBtn) / 2, (int) (y + height - 23), new Color(190, 190, 190, alpha).getRGB(), false);
            if (attributesChanged) {
                Text textNeedPoints = Text.of(Constants.NEED.getString() + " " + pointsToUpgrade + " " + Constants.POINTS.getString());
                context.drawText(MinecraftClient.getInstance().textRenderer, textNeedPoints, (int) (x + 10 + (double) 180 / 2) - textRenderer.getWidth(textNeedPoints) / 2, (int) (y + height - 45), new Color(190, 190, 190, alpha).getRGB(), false);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            PlayerData playerData = DataProvider.get(PlayerData.class);
            if (playerData != null) {
                Skill skill_ = playerData.getSkill(this.skill.getId());
                double scaledHeight = MinecraftClient.getInstance()
                        .getWindow()
                        .getScaledHeight();
                double scaledWidth = MinecraftClient.getInstance()
                        .getWindow()
                        .getScaledWidth();
                double height = (125 + this.skill.getAttributes().size() * 10);
                double x = scaledWidth / 2 - 200 / 2;
                double y = scaledHeight / 2 - height / 2;
                int textY = 10;
                for (Attribute<?> tempAttribute : tempAttributes) {
                    Attribute<?> attribute = skill_.getAttribute(tempAttribute.getIdentifier());
                    if (Util.pointedTo(x + 200 - 25, y + 20 + textY - 5, 15, 15, mouseX, mouseY)) { // add
                        if (Util.getMaxLevelAttribute((NumberAttribute) tempAttribute) > tempAttribute.getLevel())
                            tempAttribute.setLevel(tempAttribute.getLevel() + 1);
                    } else if (Util.pointedTo(x + 200 - 45, y + 20 + textY - 5, 15, 15, mouseX, mouseY)) { // sub
                        if (tempAttribute.getLevel() > attribute.getLevel())
                            tempAttribute.setLevel(tempAttribute.getLevel() - 1);
                    }
                    textY += MinecraftClient.getInstance().textRenderer.fontHeight + 7;
                }
                attributesChanged = !attributesEquals(playerData);
                pointsToUpgrade = getPointsToUpgrade(playerData);
                if (Util.pointedTo(x + 10, y + height - 30, 180, 20, mouseX, mouseY)) {
                    if (playerData.getPoints() > pointsToUpgrade && pointsToUpgrade != 0) {
                        for (Attribute<?> tempAttribute : tempAttributes) {
                            Attribute<?> attribute = playerData.getSkill(this.skill.getId()).getAttribute(tempAttribute.getIdentifier());
                            for (int i = 0; i < tempAttribute.getLevel() - attribute.getLevel(); i++)
                                Network.send(new PumpSkillAttributePayload(this.skill.getId(), attribute.getIdentifier()));
                        }
                        if (MinecraftClient.getInstance().player != null)
                            MinecraftClient.getInstance().player.playSound(CustomSound.UP_ATTRIBUTE, 50f, 1.0f);
                        this.close();
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null)
            this.client.setScreen(parent);
    }

    private boolean attributesEquals(PlayerData playerData) {
        for (Attribute<?> tempAttribute : tempAttributes) {
            Attribute<?> attribute = playerData.getSkill(this.skill.getId()).getAttribute(tempAttribute.getIdentifier());
            if (attribute.getLevel() < tempAttribute.getLevel()) return false;
        }
        return true;
    }

    private int getPointsToUpgrade(PlayerData playerData) {
        int points = 0;
        for (Attribute<?> tempAttribute : tempAttributes) {
            Attribute<?> attribute = playerData.getSkill(this.skill.getId()).getAttribute(tempAttribute.getIdentifier());
            points += (tempAttribute.getLevel() - attribute.getLevel()) * attribute.getPrice();
        }
        return points;
    }

    private void renderLevelAndPointsInfo(DrawContext context, PlayerData playerData) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text pointsText = Text.of(Constants.YOUR_POINTS.getString() + ": " + playerData.getPoints());
        Text levelText = Text.of(Constants.YOUR_LEVEL.getString() + ": " + playerData.getLevel());
        int maxWidth = Math.max(textRenderer.getWidth(pointsText), textRenderer.getWidth(levelText));
        RenderUtil.renderRectangle(context, 10, 10, maxWidth + 10, textRenderer.fontHeight + 23, new Color(40, 40, 40, 200));
        RenderUtil.renderBorder(context, 10, 10, maxWidth + 10, textRenderer.fontHeight + 23, 1.0, new Color(50, 50, 50));
        context.drawText(MinecraftClient.getInstance().textRenderer, pointsText, 15, 15, new Color(190, 190, 190).getRGB(), false);
        context.drawText(MinecraftClient.getInstance().textRenderer, levelText, 15, 20 + textRenderer.fontHeight, new Color(190, 190, 190).getRGB(), false);
    }

    private void renderPoints(DrawContext context, double scaledHeight, double scaledWidth) {
        int position = 0;
        for (int y = 0; y < scaledHeight; y+=30) {
            boolean scale = horizontalAnimationPosition == position;
            if (System.currentTimeMillis() - horizontalAnimationLastUpdateTime >= 100) {
                horizontalAnimationPosition++;
                horizontalAnimationLastUpdateTime = System.currentTimeMillis();
            }
            for (int x = 0; x < scaledWidth; x+=30) {
                RenderUtil.renderRectangle(context, x, y, scale ? 2 : 1, scale ? 2 : 1, new Color(128, 128, 128, 128));
            }
            position++;
        }
        if (horizontalAnimationPosition >= scaledHeight / 30)
            horizontalAnimationPosition = 0;
    }
}
