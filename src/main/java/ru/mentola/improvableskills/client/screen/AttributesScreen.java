package ru.mentola.improvableskills.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import ru.mentola.improvableskills.skill.Skill;

public final class AttributesScreen extends Screen {
    private final Skill skill;
    private final Screen parent;

    public AttributesScreen(Skill skill, Screen parent) {
        super(Text.of("Attributes Screen"));

        this.skill = skill;
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
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