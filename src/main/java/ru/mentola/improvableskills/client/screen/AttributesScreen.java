package ru.mentola.improvableskills.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import ru.mentola.improvableskills.attribute.NumberAttribute;
import ru.mentola.improvableskills.client.animation.Animation;
import ru.mentola.improvableskills.client.animation.SquareAnimation;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.client.notice.Notice;
import ru.mentola.improvableskills.client.screen.background.ScreenBackground;
import ru.mentola.improvableskills.client.screen.draggable.Draggable;
import ru.mentola.improvableskills.client.screen.uielement.AttributeElement;
import ru.mentola.improvableskills.client.screen.uielement.AttributeLevelElement;
import ru.mentola.improvableskills.client.screen.uielement.ButtonElement;
import ru.mentola.improvableskills.client.sound.CustomSound;
import ru.mentola.improvableskills.client.util.RenderUtil;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.PumpSkillAttributePayload;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.util.Util;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class AttributesScreen extends Screen {
    private final ScreenBackground screenBackground = new ScreenBackground(30, 30);
    private final Draggable draggable = new Draggable();

    private final List<AttributeElement> attributeElements = new LinkedList<>();
    private final Set<Animation> animations = new LinkedHashSet<>();
    private final Map<AttributeElement, List<AttributeLevelElement>> cachedLevelElements = new HashMap<>();
    private final Screen parent;

    private AttributeLevelElement selectedLevelElement;
    private ButtonElement upgradeButton;

    private int widthInfoBox = 150;
    private int heightInfoBox = 100;

    private double windowScaledHeight, windowScaledWidth;
    private PlayerData playerData;
    private Skill skill;

    // notices
    private final Set<Notice> localNoticeQueue = new LinkedHashSet<>();

    public AttributesScreen(Skill skill, Screen parent) {
        super(Text.of("Attributes Screen"));
        this.skill = skill;
        this.parent = parent;
        this.attributeElements.addAll(this.skill.getAttributes()
                .stream()
                .map(attribute -> new AttributeElement(this.skill, attribute, 0, 0, 32, 32))
                .toList());
        upgradeButton = new ButtonElement(Text.translatable("gui.improvableskills.improvescreen.upgrade"), 0, 0, 100, 20, () -> {
            if (this.selectedLevelElement != null) {
                int needPoints = this.selectedLevelElement.getAttribute().getPrice() * (this.selectedLevelElement.getLevel() - this.skill.getAttribute(this.selectedLevelElement.getAttribute().getIdentifier()).getLevel());
                if (this.playerData.getPoints() < needPoints) {
                    this.localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.needpoints").getString() + ": " + needPoints, Constants.WARNING_TEX));
                    return;
                }
                if (this.skill.getAttribute(this.selectedLevelElement.getAttribute().getIdentifier()).getLevel() >= this.selectedLevelElement.getLevel()) {
                    this.localNoticeQueue.add(new Notice(Text.translatable("gui.improvableskills.improveattributesscreen.existslvlattribute").getString(), Constants.WARNING_TEX));
                    return;
                }
                Network.send(new PumpSkillAttributePayload(this.skill.getId(), this.selectedLevelElement.getAttribute().getIdentifier(), this.selectedLevelElement.getLevel() - this.skill.getAttribute(this.selectedLevelElement.getAttribute().getIdentifier()).getLevel()));
                animations.add(new SquareAnimation(Identifier.of("square-animation-pump-skill-attribute"), 750, 50, 1, 200));
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.playSound(CustomSound.UP_ATTRIBUTE, 50f, 1.0f);
            }
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.playerData = DataProvider.get(PlayerData.class);
        super.render(context, mouseX, mouseY, delta);
        if (this.playerData != null) {
            animations.removeIf(Animation::isDie);
            this.skill = playerData.getSkill(this.skill.getId());
            this.windowScaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
            this.windowScaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
            context.enableScissor(0, 0, (int) windowScaledWidth, (int) windowScaledHeight);
            this.screenBackground.render(context);
            this.renderAttributeArea(context, mouseX, mouseY, delta);
            ImproveScreen.renderHeader(context, playerData, windowScaledHeight, windowScaledWidth);
            this.renderInfoBox(context, mouseX, mouseY, delta);
            this.renderNotices(context);
            for (Animation animation : animations)
                animation.render(context, mouseX, mouseY);
            context.disableScissor();
        }
    }

    private void renderInfoBox(DrawContext context, int mouseX, int mouseY, float delta) {
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
        Text nameAttribute = this.selectedLevelElement == null ? Text.translatable("gui.improvableskills.improvescreen.infoattribute") : Text.of(selectedLevelElement.getAttribute().getName().getString());
        Util.renderCenteredText(context, nameAttribute, windowScaledWidth - widthInfoBox - 20, windowScaledHeight - heightInfoBox - 20, widthInfoBox, 20, new Color(190, 190, 190, 200));
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (this.selectedLevelElement != null) {
            int offsetDescTextY = (int) (windowScaledHeight - heightInfoBox) + 10;
            RenderUtil.drawTexture(context, Constants.DESCRIPTION_TEX, (int) (windowScaledWidth - widthInfoBox) - 10, offsetDescTextY + ((float) (textRenderer.fontHeight) / 2 - (float) 8 / 2) - 1, 0, 0, 8, 8, new Color(255, 255, 255, 200));
            context.drawText(textRenderer, Text.translatable("gui.improvableskills.improvescreen.description"), (int) (windowScaledWidth - widthInfoBox) - 10 + 11, offsetDescTextY, new Color(217, 217, 217).getRGB(), false);
            offsetDescTextY += textRenderer.fontHeight;
            for (OrderedText row : textRenderer.wrapLines(selectedLevelElement.getAttribute().getDesc(), widthInfoBox - 20)) {
                context.drawText(textRenderer, row, (int) (windowScaledWidth - widthInfoBox) - 10, offsetDescTextY, new Color(190, 190, 190, 200).getRGB(), false);
                offsetDescTextY += textRenderer.fontHeight;
            }
            offsetDescTextY += textRenderer.fontHeight;
            context.drawText(textRenderer,
                    Text.of(String.format(Text.translatable("gui.improvableskills.improvescreen.change").getString() + " %.1f -> %.1f", Util.getNumberValueAttribute((NumberAttribute<?>) this.playerData.getSkill(this.skill.getId()).getAttribute(this.selectedLevelElement.getAttribute().getIdentifier())).doubleValue(), Util.getNumberValueAttribute((NumberAttribute<?>) this.selectedLevelElement.getAttribute(), this.selectedLevelElement.getLevel()).doubleValue())),
                    ((int) (windowScaledWidth - widthInfoBox) - 10),
                    offsetDescTextY,
                    new Color(190, 190, 190, 200).getRGB(), false);
        }
        RenderUtil.renderBorder(
                context,
                windowScaledWidth - widthInfoBox - 20,
                windowScaledHeight - heightInfoBox - 20,
                widthInfoBox,
                heightInfoBox,
                1.0,
                new Color(50, 50, 50, 200));
        if (selectedLevelElement != null) {
            upgradeButton.setX(windowScaledWidth - widthInfoBox - 10);
            upgradeButton.setY(windowScaledHeight - upgradeButton.getHeight() - 30);
            upgradeButton.setWidth(widthInfoBox - 20);
            upgradeButton.onRender(context, mouseX, mouseY, delta);
        }
    }

    private void renderAttributeArea(DrawContext context, int mouseX, int mouseY, float delta) {
        int offsetY = 160;
        int offsetXLeft = (int) draggable.getOffsetX() + 240;
        int offsetXRight = (int) draggable.getOffsetX() + (int) windowScaledWidth - 240;
        for (int i = 0; i < attributeElements.size(); i++) {
            AttributeElement element = attributeElements.get(i);
            int elementX = i % 2 == 0 ? offsetXRight : offsetXLeft;
            int elementY = (int) draggable.getOffsetY() + offsetY;
            element.setX(elementX);
            element.setY(elementY);
            offsetY += 160;
            int maxLevel = Util.getMaxLevelAttribute((NumberAttribute<?>) element.getAttribute());
            renderAttributeLevels(context, element, maxLevel, mouseX, mouseY, delta);
            element.onRender(context, mouseX, mouseY, delta);
        }
    }

    private void renderAttributeLevels(DrawContext context, AttributeElement element, int maxLevel, int mouseX, int mouseY, float delta) {
        List<AttributeLevelElement> levels = getAttributeLevelElements(element, maxLevel);
        double radius = 65;
        double centerX = element.getX() + element.getWidth() / 2.0;
        double centerY = element.getY() + element.getHeight() / 2.0;
        int levelWidth = 16;
        int levelHeight = 16;
        for (AttributeLevelElement level : levels) {
            double angle = level.getLevel() * 2 * Math.PI / maxLevel;
            double levelX = centerX + Math.cos(angle) * radius - levelWidth / 2;
            double levelY = centerY + Math.sin(angle) * radius - levelHeight / 2;
            level.setX(levelX);
            level.setY(levelY);
            if (this.selectedLevelElement != null) {
                level.setSelected(this.selectedLevelElement.getAttribute().getIdentifier().equals(level.getAttribute().getIdentifier()) && this.selectedLevelElement.getLevel() == level.getLevel());
            } else {
                level.setSelected(false);
            }
            level.setAttached(this.skill.getAttribute(element.getAttribute().getIdentifier()).getLevel() >= level.getLevel());
            level.onRender(context, mouseX, mouseY, delta);
        }
    }

    private @NotNull List<AttributeLevelElement> getAttributeLevelElements(AttributeElement element, int maxLevel) {
        if (cachedLevelElements.containsKey(element))
            return cachedLevelElements.get(element);
        List<AttributeLevelElement> levels = new ArrayList<>(maxLevel);
        double radius = 65;
        double centerX = element.getX() + element.getWidth() / 2.0;
        double centerY = element.getY() + element.getHeight() / 2.0;
        int levelWidth = 16;
        int levelHeight = 16;
        for (int i = 0; i < maxLevel; i++) {
            double angle = i * 2 * Math.PI / maxLevel;
            double levelX = centerX + Math.cos(angle) * radius - levelWidth / 2;
            double levelY = centerY + Math.sin(angle) * radius - levelHeight / 2;
            AttributeLevelElement levelElement = new AttributeLevelElement(element.getAttribute(), i + 1, levelX, levelY, levelWidth, levelHeight);
            levels.add(levelElement);
        }
        cachedLevelElements.put(element, levels);
        return levels;
    }

    private void calculateInfoBoxDimensions() {
        int minHeight = 100;
        int minWidth = 150;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int titleHeight = textRenderer.fontHeight;
        int descriptionHeight = this.selectedLevelElement != null ? textRenderer.wrapLines(this.selectedLevelElement.getAttribute().getDesc(), minWidth - 20).size() * textRenderer.fontHeight : 0;
        int calculatedHeight = titleHeight + descriptionHeight + 90;
        this.heightInfoBox = Math.max(minHeight, calculatedHeight);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggable.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        draggable.mouseClicked(mouseX, mouseY, button);
        if (button == 0) {
            if (Util.pointedTo(windowScaledWidth - widthInfoBox - 20, windowScaledHeight - heightInfoBox - 20, windowScaledWidth, heightInfoBox, mouseX, mouseY)) {
                if (selectedLevelElement != null) {
                    if (upgradeButton.isHovered())
                        upgradeButton.getRunnable().run();
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }
            for (Map.Entry<AttributeElement, List<AttributeLevelElement>> attributeElement : this.cachedLevelElements.entrySet()) {
                for (AttributeLevelElement levelElement : attributeElement.getValue()) {
                    if (levelElement.isHovered() && !levelElement.isAttached()) {
                        selectedLevelElement = levelElement;
                        this.calculateInfoBoxDimensions();
                        return super.mouseClicked(mouseX, mouseY, button);
                    }
                }
            }
            selectedLevelElement = null;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderNotices(DrawContext context) {
        localNoticeQueue.removeIf((notice) -> !notice.isAlive());
        for (Notice notice : localNoticeQueue) notice.render(context);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        draggable.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}