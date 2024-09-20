package ru.mentola.improvableskills.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.data.client.provider.DataProvider;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.PumpLevelPayload;
import ru.mentola.improvableskills.network.payload.PumpSkillPayload;
import ru.mentola.improvableskills.screen.element.SkillElement;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.base.Skill;
import ru.mentola.improvableskills.skill.attribute.Attribute;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.util.RenderUtil;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;
import java.util.List;

public final class ImproveScreen extends Screen {
    private final List<SkillElement> skillElements;

    private SkillElement selectedSkill;
    private SkillElement movedSkill;

    private double offsetX = 0;
    private double offsetY = 0;
    private double lastMouseX;
    private double lastMouseY;
    private boolean isDragging = false;

    private long horizontalAnimationLastUpdateTime = 0;
    private int horizontalAnimationPosition = 0;

    // up level animation
    private boolean upAnimation = false;
    private long upAnimationStartTime = 0;
    private final int upAnimationDuration = 750;
    private final int numberOfSquares = 100;
    private final int squareSize = 1;
    private final int maxDistance = 200;
    private final Point[] squarePositions = new Point[numberOfSquares];
    private final double[] velocitiesX = new double[numberOfSquares];
    private final double[] velocitiesY = new double[numberOfSquares];

    public ImproveScreen() {
        super(Text.of("Improve Screen"));

        this.skillElements = SkillProvider.getSkills()
                .stream()
                .map((skill) -> new SkillElement(skill.getTex(), skill))
                .toList();
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

        this.renderPoints(context, scaledHeight, scaledWidth);
        context.enableScissor(0, 0, (int) scaledWidth, (int) scaledHeight);
        this.renderTreeArea(context, scaledHeight, scaledWidth, mouseX, mouseY);
        context.disableScissor();
        this.renderInfo(context, mouseX, mouseY, scaledHeight, scaledWidth);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double scaledHeight = MinecraftClient.getInstance()
                .getWindow()
                .getScaledHeight();
        double scaledWidth = MinecraftClient.getInstance()
                .getWindow()
                .getScaledWidth();
        PlayerData playerData = DataProvider.get(PlayerData.class);
        if (Util.pointedTo(scaledWidth - 170, scaledHeight - 220, 150, 200, mouseX, mouseY)) {
            if (playerData != null && selectedSkill != null) {
                if (Util.pointedTo(scaledWidth - 160, scaledHeight - 50, 130, 20, mouseX, mouseY)
                        && button == 0) {
                    if (!playerData.containsSkill(selectedSkill.getSkill().getId()))
                        if (playerData.getPoints() >= selectedSkill.getSkill().getPricePoints()) {
                            Network.send(new PumpSkillPayload(selectedSkill.getSkill().getId()));
                        }
                } else if (Util.pointedTo(scaledWidth - 160, scaledHeight - 75, 130, 20, mouseX, mouseY)
                        && button == 0) {
                    if (playerData.containsSkill(selectedSkill.getSkill().getId()))
                        if (this.client != null)
                            this.client.setScreen(new ImproveAttributesScreen(this, playerData.getSkill(selectedSkill.getSkill().getId())));
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        if (playerData != null) {
            Text pointsText = Text.of(Constants.YOUR_POINTS.getString() + ": " + playerData.getPoints());
            Text levelText = Text.of(Constants.YOUR_LEVEL.getString() + ": " + playerData.getLevel());
            int maxWidth = Math.max(textRenderer.getWidth(pointsText), textRenderer.getWidth(levelText));
            if (Util.pointedTo(25 + maxWidth, 10, 32, 32, mouseX, mouseY)) {
                if (Util.getNextPointsToNextLevelNeed(playerData) < playerData.getPoints()) {
                    Network.send(new PumpLevelPayload());
                    upAnimation = true;
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
        if (button == 0
                && movedSkill == null) {
            this.isDragging = true;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
        if (!skillElements.isEmpty()) {
            int startX = (int) (offsetX);
            int startY = (int) (offsetY);
            int position = 0;
            for (int y = 100; y < MinecraftClient.getInstance().getWindow().getScaledHeight() - 100; y+=96) {
                for (int x = 100; x < MinecraftClient.getInstance().getWindow().getScaledWidth() - 100; x+=96) {
                    if (Util.pointedTo(startX + x, startY + y, 32, 32, mouseX, mouseY)) {
                        SkillElement skillElement = skillElements.get(position);
                        skillElement.mouseClicked(button);
                        selectedSkill = skillElement;
                        return super.mouseClicked(mouseX, mouseY, button);
                    }
                    position++;
                    if (position == skillElements.size()) {
                        selectedSkill = null;
                        return super.mouseClicked(mouseX, mouseY, button);
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isDragging) this.isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging && button == 0) {
            this.offsetX += (mouseX - this.lastMouseX);
            this.offsetY += (mouseY - this.lastMouseY);
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void renderTreeArea(DrawContext context, double scaledHeight, double scaledWidth, int mouseX, int mouseY) {
        movedSkill = null;
        int movedX = 0;
        if (!skillElements.isEmpty()) {
            int startX = (int) (offsetX);
            int startY = (int) (offsetY);
            int position = 0;
            for (int y = 100; y < scaledHeight - 100; y+=96) {
                for (int x = 100; x < scaledWidth - 100; x+=96) {
                    SkillElement element = skillElements.get(position);
                    if (Util.pointedTo(startX + x, startY + y, 32, 32, mouseX, mouseY)) {
                        movedSkill = element;
                        movedX = x;
                    }
                    element.render(context, startX + x, startY + y, selectedSkill == element);
                    position++;
                    if (position == skillElements.size()) break;
                }
                if (position == skillElements.size()) break;
            }
            if (movedSkill != selectedSkill
                    && movedSkill != null) {
                RenderUtil.renderRectangle(context, startX + movedX + 40, mouseY - 30 / 2, 200, 30, new Color(40, 40, 40, 200));
                context.drawText(MinecraftClient.getInstance().textRenderer, Constants.PRESS_TO_MORE, (int) (startX + movedX + 40 + (double) 200 / 2) - textRenderer.getWidth(Constants.PRESS_TO_MORE) / 2, (int) ((mouseY - 30 / 2) + 10), new Color(190, 190, 190).getRGB(), false);
                RenderUtil.renderBorder(context, startX + movedX + 40, mouseY - 30 / 2, 200, 30, 1, new Color(50, 50, 50));
            }
        }
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

    private void renderInfo(DrawContext context, double mouseX, double mouseY, double scaledHeight, double scaledWidth) {
        PlayerData playerData = DataProvider.get(PlayerData.class);
        if (playerData != null) {
            this.renderLevelAndPointsInfo(context, playerData, mouseX, mouseY);
            RenderUtil.renderRectangle(context, scaledWidth - 170, scaledHeight - 220, 150, 200, new Color(40, 40, 40, 200));
            RenderUtil.renderRectangle(context, scaledWidth - 170, scaledHeight - 220, 150, 20, new Color(30, 30, 30, 200));
            Text title = selectedSkill == null ? Constants.INFO_SKILL : selectedSkill.getSkill().getName();
            context.drawText(MinecraftClient.getInstance().textRenderer, title, (int) (scaledWidth - 170 + (double) 150 / 2) - textRenderer.getWidth(title) / 2, (int) (scaledHeight - 213), new Color(190, 190, 190).getRGB(), false);
            RenderUtil.renderBorder(context, scaledWidth - 170, scaledHeight - 220, 150, 200, 1, new Color(50, 50, 50));
            if (selectedSkill != null) {
                int maxWidth = 130;
                int textY = 10;
                List<OrderedText> wrappedText = MinecraftClient.getInstance()
                        .textRenderer
                        .wrapLines(selectedSkill.getSkill().getDescription(), maxWidth);
                for (OrderedText orderedText : wrappedText) {
                    context.drawText(MinecraftClient.getInstance().textRenderer, orderedText, (int) (scaledWidth - 160), (int) (scaledHeight - 200 + textY), new Color(190, 190, 190).getRGB(), false);
                    textY += MinecraftClient.getInstance().textRenderer.fontHeight;
                }
                if (playerData.containsSkill(selectedSkill.getSkill().getId())) {
                    textY += 10;
                    Skill skill = playerData.getSkill(selectedSkill.getSkill().getId());
                    context.drawText(MinecraftClient.getInstance().textRenderer, Constants.ATTRIBUTES, (int) (scaledWidth - 160), (int) (scaledHeight - 200 + textY), new Color(250, 179, 43).getRGB(), false);
                    for (Attribute<?> attribute : skill.getAttributes()) {
                        textY += textRenderer.fontHeight + 2;
                        context.drawText(MinecraftClient.getInstance().textRenderer, Text.of(attribute.getName().getString() + " " + attribute.getLevel()), (int) (scaledWidth - 160), (int) (scaledHeight - 200 + textY), new Color(190, 190, 190).getRGB(), false);
                    };
                }
                this.renderAttachSkillButtonAndInfo(context, playerData, scaledHeight, scaledWidth);
            }
        }
    }

    private void renderAttachSkillButtonAndInfo(DrawContext context, PlayerData playerData, double scaledHeight, double scaledWidth) {
        boolean hasExistsSkill = playerData.containsSkill(this.selectedSkill.getSkill().getId());
        int alpha = hasExistsSkill ? 50 : (playerData.getPoints() < this.selectedSkill.getSkill().getPricePoints() ? 50 : 200);
        Text text = hasExistsSkill ? Constants.ATTACHED : (this.selectedSkill.getSkill().getPricePoints() <= playerData.getPoints() ? Constants.ATTACH : Constants.LESS_POINTS);
        RenderUtil.renderRectangle(context, scaledWidth - 160, scaledHeight - 50, 130, 20, new Color(30, 30, 30, alpha));
        RenderUtil.renderBorder(context, scaledWidth - 160, scaledHeight - 50, 130, 20, 1, new Color(50, 50, 50, alpha));
        context.drawText(MinecraftClient.getInstance().textRenderer, text, (int) (scaledWidth - 160 + (double) 130 / 2) - textRenderer.getWidth(text) / 2, (int) (scaledHeight - 43), new Color(190, 190, 190, alpha).getRGB(), false);
        if (!hasExistsSkill) {
            Text attachInfoText = Constants.NEED_TO_ATTACH;
            Text attachPriceText = Text.of(this.selectedSkill.getSkill().getPricePoints() + " " + Constants.POINTS.getString() + ", " + this.selectedSkill.getSkill().getNeedLevel() + " " + Constants.LEVEL.getString());
            context.drawText(MinecraftClient.getInstance().textRenderer, attachInfoText, (int) (scaledWidth - 170 + (double) 150 / 2) - textRenderer.getWidth(attachInfoText) / 2, (int) (scaledHeight - 53) - textRenderer.fontHeight * 2 - 2, new Color(190, 190, 190).getRGB(), false);
            context.drawText(MinecraftClient.getInstance().textRenderer, attachPriceText, (int) (scaledWidth - 170 + (double) 150 / 2) - textRenderer.getWidth(attachPriceText) / 2, (int) (scaledHeight - 53) - textRenderer.fontHeight, new Color(190, 190, 190).getRGB(), false);
        } else {
            RenderUtil.renderRectangle(context, scaledWidth - 160, scaledHeight - 75, 130, 20, new Color(30, 30, 30, 200));
            RenderUtil.renderBorder(context, scaledWidth - 160, scaledHeight - 75, 130, 20, 1, new Color(50, 50, 50, 200));
            context.drawText(MinecraftClient.getInstance().textRenderer, Constants.UPGRADE, (int) (scaledWidth - 160 + (double) 130 / 2) - textRenderer.getWidth(Constants.UPGRADE) / 2, (int) (scaledHeight - 68), new Color(190, 190, 190, 200).getRGB(), false);
        }
    }

    private void renderLevelAndPointsInfo(DrawContext context, PlayerData playerData, double mouseX, double mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text pointsText = Text.of(Constants.YOUR_POINTS.getString() + ": " + playerData.getPoints());
        Text levelText = Text.of(Constants.YOUR_LEVEL.getString() + ": " + playerData.getLevel());
        int maxWidth = Math.max(textRenderer.getWidth(pointsText), textRenderer.getWidth(levelText));
        RenderUtil.renderRectangle(context, 10, 10, maxWidth + 10, textRenderer.fontHeight + 23, new Color(40, 40, 40, 200));
        RenderUtil.renderBorder(context, 10, 10, maxWidth + 10, textRenderer.fontHeight + 23, 1.0, new Color(50, 50, 50));
        context.drawText(MinecraftClient.getInstance().textRenderer, pointsText, 15, 15, new Color(190, 190, 190).getRGB(), false);
        context.drawText(MinecraftClient.getInstance().textRenderer, levelText, 15, 20 + textRenderer.fontHeight, new Color(190, 190, 190).getRGB(), false);
        // render up level btn
        int alpha = Util.getNextPointsToNextLevelNeed(playerData) <= playerData.getPoints() ? 200 : 50;
        RenderUtil.renderRectangle(context, 25 + maxWidth, 10, 32, 32, new Color(40, 40, 40, alpha));
        RenderUtil.renderBorder(context, 25 + maxWidth, 10, 32, 32, 1.0, new Color(50, 50, 50, alpha));
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.of("UP!"), ((25 + maxWidth) + 32 / 2) - textRenderer.getWidth(Text.of("UP!")) / 2, 13 + textRenderer.fontHeight, new Color(190, 190, 190, alpha).getRGB(), false);
        if (Util.pointedTo(25 + maxWidth, 10, 32, 32, mouseX, mouseY)) {
            RenderUtil.renderRectangle(context, 25 + maxWidth + 40, mouseY - 30 / 2, 150, 30, new Color(40, 40, 40, 200));
            String text;
            if (Util.getNextPointsToNextLevelNeed(playerData) > playerData.getPoints()) {
                text = Constants.NEED.getString() + " " + Util.getNextPointsToNextLevelNeed(playerData) + " " + Constants.POINTS.getString();
            } else {
                text = Constants.UPGRADE.getString();
            }
            context.drawText(MinecraftClient.getInstance().textRenderer, text, (int) (25 + maxWidth + 40 + (double) 150 / 2) - textRenderer.getWidth(text) / 2, (int) ((mouseY - 30 / 2) + 10), new Color(190, 190, 190).getRGB(), false);
            RenderUtil.renderBorder(context, 25 + maxWidth + 40, mouseY - 30 / 2, 150, 30, 1, new Color(50, 50, 50));
        }
        // render up animation
        if (upAnimation) {
            if (upAnimationStartTime == 0) {
                upAnimationStartTime = System.currentTimeMillis();
                for (int i = 0; i < numberOfSquares; i++) {
                    squarePositions[i] = new Point((int) mouseX, (int) mouseY);
                    velocitiesX[i] = (Math.random() * 2 - 1) * Util.randomNumber(maxDistance, 10) / upAnimationDuration;
                    velocitiesY[i] = (Math.random() * 2 - 1) * Util.randomNumber(maxDistance, 10) / upAnimationDuration;
                }
            }
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - upAnimationStartTime;
            if (elapsedTime < upAnimationDuration) {
                for (int i = 0; i < numberOfSquares; i++) {
                    int x = (int) (squarePositions[i].x + velocitiesX[i] * elapsedTime);
                    int y = (int) (squarePositions[i].y + velocitiesY[i] * elapsedTime);
                    int alpha_ = 255 - (int) (255 * ((double) elapsedTime / upAnimationDuration));
                    RenderUtil.renderRectangle(context, x, y, squareSize, squareSize, new Color(255, 255, 255, alpha_));
                }
            } else {
                upAnimation = false;
                upAnimationStartTime = 0;
            }
        }
    }
}