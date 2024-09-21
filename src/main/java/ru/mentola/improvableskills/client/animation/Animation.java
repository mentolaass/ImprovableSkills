package ru.mentola.improvableskills.client.animation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@AllArgsConstructor @Getter
public abstract class Animation {
    private final Identifier identifier;

    private final int workTime;
    private final long startTime;

    public abstract void render(DrawContext context, double mouseX, double mouseY);

    public boolean isDie() {
        return System.currentTimeMillis() - startTime >= workTime;
    }
}