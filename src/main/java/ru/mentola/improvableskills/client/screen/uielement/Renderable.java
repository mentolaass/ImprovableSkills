package ru.mentola.improvableskills.client.screen.uielement;

import net.minecraft.client.gui.DrawContext;

public interface Renderable {
    void onRender(DrawContext context, int mouseX, int mouseY, float delta);
}
