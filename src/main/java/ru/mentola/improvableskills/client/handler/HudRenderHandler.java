package ru.mentola.improvableskills.client.handler;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import ru.mentola.improvableskills.client.notice.NoticeQueue;

public final class HudRenderHandler implements
        HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        NoticeQueue.render(drawContext);
    }
}
