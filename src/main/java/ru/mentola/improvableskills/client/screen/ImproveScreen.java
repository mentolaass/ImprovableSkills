package ru.mentola.improvableskills.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.client.animation.Animation;
import ru.mentola.improvableskills.client.animation.SquareAnimation;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.client.notice.Notice;
import ru.mentola.improvableskills.client.screen.background.ScreenBackground;
import ru.mentola.improvableskills.client.screen.draggable.Draggable;
import ru.mentola.improvableskills.client.screen.uielement.ButtonElement;
import ru.mentola.improvableskills.client.screen.uielement.SkillElement;
import ru.mentola.improvableskills.client.sound.CustomSound;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.PumpLevelPayload;
import ru.mentola.improvableskills.network.payload.PumpSkillPayload;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class ImproveScreen extends Screen {
    private final ScreenBackground screenBackground = new ScreenBackground(30, 30);
    private final Set<Animation> animations = new LinkedHashSet<>();
    private final Draggable draggable = new Draggable();

    private double windowScaledHeight, windowScaledWidth;

    // ui
    private final List<SkillElement> skillElements = new LinkedList<>();
    private final ButtonElement attachSkillButton;
    private final ButtonElement upgradeAttributesButton;
    private final ButtonElement upgradeLevelButton;

    // other
    private PlayerData playerData;
    private SkillElement selectedSkillElement;

    // info selected skill
    private int widthInfoBox = 150;
    private int heightInfoBox = 200;

    // notices
    private final Set<Notice> localNoticeQueue = new LinkedHashSet<>();

    public ImproveScreen() {
        super(Text.of("Improve Screen"));

        attachSkillButton = new ButtonElement(Text.translatable("gui.improvableskills.improvescreen.attach"), 0, 0, 100, 20, () -> {
            if (playerData.containsSkill(selectedSkillElement.getSkill().getId())) {
                localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.alreadylearned").getString(), Constants.WARNING_TEX));
                return;
            } else if (playerData.getLevel() < selectedSkillElement.getSkill().getNeedLevel()) {
                localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.needlevel").getString() + " : " + selectedSkillElement.getSkill().getNeedLevel(), Constants.WARNING_TEX));
                return;
            } else if (playerData.getPoints() < selectedSkillElement.getSkill().getPricePoints()) {
                localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.needpoints").getString() + " : " + selectedSkillElement.getSkill().getPricePoints(), Constants.WARNING_TEX));
                return;
            }
            Network.send(new PumpSkillPayload(selectedSkillElement.getSkill().getId()));
            animations.add(new SquareAnimation(Identifier.of("square-animation-pump-skill"), 750, 50, 1, 200));
            if (MinecraftClient.getInstance().player != null)
                MinecraftClient.getInstance().player.playSound(CustomSound.ATTACH_SKILL, 50f, 1.0f);
        });
        upgradeAttributesButton = new ButtonElement(Text.translatable("gui.improvableskills.improveattributesscreen.upgradeattributes"), 0, 0, 100, 20, () -> MinecraftClient.getInstance().setScreen(new AttributesScreen(this.selectedSkillElement.getSkill(), this)));
        upgradeLevelButton = new ButtonElement(Text.translatable("gui.improvableskills.improvescreen.upgradelevel"), 0, 0, 150, 30, () ->  {
            int needPoints = Util.getNextPointsToNextLevelNeed(this.playerData);
            if (playerData.getPoints() < needPoints) {
                localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.needpoints").getString() + " : " + needPoints, Constants.WARNING_TEX));
                return;
            }
            Network.send(new PumpLevelPayload());
            animations.add(new SquareAnimation(Identifier.of("square-animation-pump-skill"), 750, 50, 1, 200));
            if (MinecraftClient.getInstance().player != null)
                MinecraftClient.getInstance().player.playSound(CustomSound.UP_LEVEL, 50f, 1.0f);
        });

        skillElements.addAll(SkillProvider.getSkills()
                .stream()
                .map((skill) -> new SkillElement(skill, 0, 0, 32, 32))
                .toList());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        playerData = DataProvider.get(PlayerData.class);
        if (playerData != null) {
            animations.removeIf(Animation::isDie);
            windowScaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
            windowScaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
            context.enableScissor(0, 0, (int) windowScaledWidth, (int) windowScaledHeight);
            screenBackground.render(context);
            this.renderUIElements(context, mouseX, mouseY, delta);
            for (Animation animation : animations)
                animation.render(context, mouseX, mouseY);
            context.disableScissor();
        }
        this.renderNotices(context);
    }

    private void renderUIElements(DrawContext context, int mouseX, int mouseY, float delta) {
        // render skills
        int offsetX = 100;
        int offsetY = 100;
        int elementsPerRow = (int) ((windowScaledWidth - 200) / 64);
        for (SkillElement skillElement : skillElements) {
            skillElement.setAttached(playerData.containsSkill(skillElement.getSkill().getId()));
            skillElement.setSelected(selectedSkillElement != null && selectedSkillElement.equals(skillElement));
            skillElement.setX(draggable.getOffsetX() + offsetX);
            skillElement.setY(draggable.getOffsetY() + offsetY);
            skillElement.onRender(context, mouseX, mouseY, delta);
            offsetX += 64;
            if ((offsetX - 100) / 64 > elementsPerRow) {
                offsetX = 100;
                offsetY += 64;
            }
        }

        RenderUtil.renderRectangle(
                context,
                windowScaledWidth - widthInfoBox - 20,
                windowScaledHeight - heightInfoBox - 20,
                widthInfoBox,
                heightInfoBox,
                new Color(40, 40, 40, 200));
        RenderUtil.renderRectangle(
                context,
                windowScaledWidth - widthInfoBox - 20,
                windowScaledHeight - heightInfoBox - 20,
                widthInfoBox,
                20,
                new Color(50, 50, 50, 200));
        // render name
        Text nameSkill = selectedSkillElement == null ? Text.translatable("gui.improvableskills.improvescreen.infoskill") : selectedSkillElement.getSkill().getName();
        Util.renderCenteredText(context, nameSkill, windowScaledWidth - widthInfoBox - 20, windowScaledHeight - heightInfoBox - 20, widthInfoBox, 20, new Color(190, 190, 190, 200));
        // render desc
        if (selectedSkillElement != null) {
            int offsetDescTextY = (int) (windowScaledHeight - heightInfoBox) + 10;
            RenderUtil.drawTexture(context, Constants.DESCRIPTION_TEX, (int) (windowScaledWidth - widthInfoBox) - 10, offsetDescTextY + ((float) (textRenderer.fontHeight) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
            context.drawText(textRenderer, Text.translatable("gui.improvableskills.improvescreen.description"), (int) (windowScaledWidth - widthInfoBox) - 10 + 11, offsetDescTextY, new Color(217, 217, 217).getRGB(), false);
            offsetDescTextY += textRenderer.fontHeight;
            for (OrderedText row : textRenderer.wrapLines(selectedSkillElement.getSkill().getDescription(), widthInfoBox - 20)) {
                context.drawText(textRenderer, row, (int) (windowScaledWidth - widthInfoBox) - 10, offsetDescTextY, new Color(190, 190, 190, 200).getRGB(), false);
                offsetDescTextY += textRenderer.fontHeight;
            }
            if (playerData.containsSkill(selectedSkillElement.getSkill().getId())) {
                offsetDescTextY += textRenderer.fontHeight;
                RenderUtil.drawTexture(context, Constants.ATTRIBUTE_TEX, (int) (windowScaledWidth - widthInfoBox) - 10, offsetDescTextY + ((float) (textRenderer.fontHeight) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
                context.drawText(textRenderer, Text.translatable("gui.improvableskills.improvescreen.attributes"), (int) (windowScaledWidth - widthInfoBox) - 10 + 11, offsetDescTextY, new Color(217, 217, 217).getRGB(), false);
                offsetDescTextY += textRenderer.fontHeight;
                for (Attribute<?> attribute : this.playerData.getSkill(this.selectedSkillElement.getSkill().getId()).getAttributes()) {
                    int x = (int) (windowScaledWidth - widthInfoBox) - 10;
                    Text row = Text.of(attribute.getName().getString() + " " + attribute.getLevel());
                    context.drawText(textRenderer, row, x, offsetDescTextY, new Color(190, 190, 190, 200).getRGB(), false);
                    RenderUtil.drawTexture(context, Constants.LEVEL_TEX, x + textRenderer.getWidth(row) + 3, offsetDescTextY + ((float) (textRenderer.fontHeight) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
                    offsetDescTextY += textRenderer.fontHeight;
                }
            }
        }
        RenderUtil.renderBorder(
                context,
                windowScaledWidth - widthInfoBox - 20,
                windowScaledHeight - heightInfoBox - 20,
                widthInfoBox,
                heightInfoBox,
                1.0,
                new Color(50, 50, 50, 200));

        // skill btns
        if (selectedSkillElement != null) {
            // attach btn
            boolean attached = playerData.containsSkill(selectedSkillElement.getSkill().getId());
            attachSkillButton.setX(windowScaledWidth - widthInfoBox - 10);
            attachSkillButton.setY(windowScaledHeight - attachSkillButton.getHeight() - 30);
            attachSkillButton.setWidth(widthInfoBox - 20);
            attachSkillButton.setText(!attached ? Text.translatable("gui.improvableskills.improvescreen.attach") : Text.translatable("gui.improvableskills.improvescreen.attached"));
            attachSkillButton.setEnabled(!attached);
            attachSkillButton.onRender(context, mouseX, mouseY, delta);
            // upgrade btn
            if (attached) {
                upgradeAttributesButton.setX(windowScaledWidth - widthInfoBox - 10);
                upgradeAttributesButton.setY(windowScaledHeight - upgradeAttributesButton.getHeight() - 55);
                upgradeAttributesButton.setWidth(widthInfoBox - 20);
                upgradeAttributesButton.setText(Text.translatable("gui.improvableskills.improveattributesscreen.upgradeattributes"));
                upgradeAttributesButton.setEnabled(true);
                upgradeAttributesButton.onRender(context, mouseX, mouseY, delta);
            }
        }
        upgradeLevelButton.setX(windowScaledWidth / 2 - upgradeLevelButton.getWidth() / 2);
        upgradeLevelButton.setY(windowScaledHeight - 60);
        upgradeLevelButton.onRender(context, mouseX, mouseY, delta);
        renderHeader(context, playerData, windowScaledHeight, windowScaledWidth);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (Util.pointedTo(windowScaledWidth - widthInfoBox - 20, windowScaledHeight - heightInfoBox - 20, windowScaledWidth, heightInfoBox, mouseX, mouseY)) {
                if (selectedSkillElement != null) {
                    if (attachSkillButton.isHovered())
                        attachSkillButton.getRunnable().run();
                    else if (upgradeAttributesButton.isHovered())
                        upgradeAttributesButton.getRunnable().run();
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
            if (upgradeLevelButton.isHovered()) {
                upgradeLevelButton.getRunnable().run();
                return super.mouseClicked(mouseX, mouseY, button);
            }
            for (SkillElement skillElement : skillElements) {
                if (skillElement.isHovered()) {
                    this.selectedSkillElement = skillElement;
                    this.calculateInfoBoxDimensions();
                    return super.mouseClicked(mouseX, mouseY, button);
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        draggable.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggable.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        draggable.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void renderNotices(DrawContext context) {
        localNoticeQueue.removeIf((notice) -> !notice.isAlive());
        for (Notice notice : localNoticeQueue) notice.render(context);
    }

    private void calculateInfoBoxDimensions() {
        int minWidth = 150;
        int minHeight = 200;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxAttributeWidth = 0;
        if (selectedSkillElement != null) {
            for (Attribute<?> attribute : selectedSkillElement.getSkill().getAttributes()) {
                Text attributeText = Text.of(attribute.getName().getString() + " " + attribute.getLevel());
                int attributeTextWidth = textRenderer.getWidth(attributeText);
                maxAttributeWidth = Math.max(maxAttributeWidth, attributeTextWidth);
            }
            maxAttributeWidth += 16;
        }
        int attributesCount = selectedSkillElement == null ? 0 : selectedSkillElement.getSkill().getAttributes().size();
        int titleHeight = textRenderer.fontHeight;
        int descriptionHeight = selectedSkillElement != null ? textRenderer.wrapLines(selectedSkillElement.getSkill().getDescription(), minWidth - 20).size() * textRenderer.fontHeight : 0;
        int attributesHeight = attributesCount * textRenderer.fontHeight;
        int calculatedHeight = titleHeight + descriptionHeight + attributesHeight + 120;
        this.widthInfoBox = Math.max(minWidth, maxAttributeWidth + 20);
        this.heightInfoBox = Math.max(minHeight, calculatedHeight);
    }

    public static void renderHeader(DrawContext context, PlayerData playerData, double windowScaledHeight, double windowScaledWidth) {
        int width = 125;
        int height = 30;
        double x = windowScaledWidth / 2 - (double) width / 2;
        RenderUtil.renderRectangle(
                context,
                x,
                -1,
                width,
                height,
                new Color(40, 40, 40, 200));
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null)
            RenderUtil.renderPlayerHead(context, player, (int) (x + 5), 4, 20, 20, 0.8f);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        // draw points
        RenderUtil.drawTexture(context, Constants.POINTS_TEX, (int) x + 30, ((float) (height - 1) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
        context.drawText(textRenderer, Text.of(String.valueOf(playerData.getPoints())), (int) x + 42, (height - 1) / 2 - textRenderer.fontHeight / 2, new Color(190, 190, 190, 200).getRGB(), false);
        // draw level
        RenderUtil.drawTexture(context, Constants.LEVEL_TEX, (int) x + 42 + textRenderer.getWidth(String.valueOf(playerData.getPoints())) + 5, ((float) (height - 1) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
        context.drawText(textRenderer, Text.of(String.valueOf(playerData.getLevel())), (int) x + 42 + textRenderer.getWidth(String.valueOf(playerData.getPoints())) + 15, (height - 1) / 2 - textRenderer.fontHeight / 2, new Color(190, 190, 190, 200).getRGB(), false);
        RenderUtil.renderBorder(
                context,
                x,
                -1,
                width,
                height,
                1.0,
                new Color(50, 50, 50, 200));
    }
}
