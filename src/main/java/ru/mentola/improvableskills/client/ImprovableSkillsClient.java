package ru.mentola.improvableskills.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.client.handler.HudRenderHandler;
import ru.mentola.improvableskills.client.notice.Notice;
import ru.mentola.improvableskills.client.notice.NoticeQueue;
import ru.mentola.improvableskills.client.screen.ImproveScreen;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.NoticePayload;

public final class ImprovableSkillsClient implements ClientModInitializer {
    public static final KeyBinding IMPROVE_SCREEN_KEYBINDING = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.improvableskills.openscreen",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT_SHIFT,
                    "category.improvableskills"));

    @Override
    public void onInitializeClient() {
        final HudRenderHandler hudRenderHandler = new HudRenderHandler();
        HudRenderCallback.EVENT.register(hudRenderHandler);

        ClientTickEvents.START_CLIENT_TICK.register((c) -> {
            while (IMPROVE_SCREEN_KEYBINDING.wasPressed())
                MinecraftClient.getInstance().setScreen(new ImproveScreen());
        });

        Network.registerClientReceiver(DataUpdatePayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataProvider.get(PlayerData.class);
            if (playerData == null) throw new RuntimeException("Client Player Data Is Null");
            playerData.copyOf(payload.getPlayerData());
        });

        Network.registerClientReceiver(NoticePayload.PAYLOAD_ID, (payload, context) -> NoticeQueue.addToQueue(new Notice(payload.getText(), payload.getIdentifier())));
    }
}
