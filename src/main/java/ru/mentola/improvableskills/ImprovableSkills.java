package ru.mentola.improvableskills;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;
import ru.mentola.improvableskills.api.ImprovableSkillsAPI;
import ru.mentola.improvableskills.api.provider.ImprovableSkillsProvider;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.data.client.provider.DataProvider;
import ru.mentola.improvableskills.data.server.DataPersistentState;
import ru.mentola.improvableskills.handler.client.HudRenderHandler;
import ru.mentola.improvableskills.handler.server.BlockHandler;
import ru.mentola.improvableskills.handler.server.EntityHandler;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.PumpSkillAttributePayload;
import ru.mentola.improvableskills.network.payload.PumpSkillPayload;
import ru.mentola.improvableskills.network.payload.side.Side;
import ru.mentola.improvableskills.screen.ImproveScreen;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.base.Skill;
import ru.mentola.improvableskills.skill.provider.SkillProvider;

public final class ImprovableSkills implements ClientModInitializer, ModInitializer, ImprovableSkillsAPI {
    public static final String MOD_ID = "improvableskills";

    public static final KeyBinding IMPROVE_SCREEN_KEYBINDING = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.improvableskills.openscreen",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.improvableskills"));

    @Override
    public void onInitialize() {
        DataProvider.initialize();

        Network.registerPayload(PumpSkillPayload.PAYLOAD_ID, PumpSkillPayload.PAYLOAD_PACKET_CODEC, Side.CLIENT);
        Network.registerPayload(PumpSkillAttributePayload.PAYLOAD_ID, PumpSkillAttributePayload.PAYLOAD_PACKET_CODEC, Side.CLIENT);
        Network.registerPayload(DataUpdatePayload.PAYLOAD_ID, DataUpdatePayload.PAYLOAD_PACKET_CODEC, Side.SERVER);

        ImprovableSkillsProvider.setAPI(this);
    }

    @Override
    public void onInitializeClient() {
        Network.registerClientReceiver(DataUpdatePayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataProvider.get(PlayerData.class);
            if (playerData == null) throw new RuntimeException("Client Player Data Is Null");
            playerData.copyOf(payload.getPlayerData());
        });

        Network.registerServerReceiver(PumpSkillPayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            if (playerData.containsSkill(payload.getIdSkill())) return;
            Skill skill = SkillProvider.getById(payload.getIdSkill());
            if (skill == null) return;
            if (playerData.getPoints() < skill.getPricePoints()
                    || playerData.getLevel() < skill.getNeedLevel()) return;
            playerData.setPoints(playerData.getPoints() - skill.getPricePoints());
            playerData.attachSkill(skill);
            Network.sendTo(context.player(), new DataUpdatePayload(playerData));
        });

        Network.registerServerReceiver(PumpSkillAttributePayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            if (!playerData.containsSkill(payload.getIdSkill())) return;
            Skill skill = playerData.getSkill(payload.getIdSkill());
            if (skill == null) return;
            if (!skill.containsAttribute(payload.getIdAttribute())) return;
            if (playerData.getPoints() < Constants.LEVEL_UP_ATTRIBUTE_PRICE) return;
            if (skill.upgradeAttribute(payload.getIdAttribute())) {
                playerData.setPoints(playerData.getPoints() - Constants.LEVEL_UP_ATTRIBUTE_PRICE);
                Network.sendTo(context.player(), new DataUpdatePayload(playerData));
            }
        });

        // register common events
        final EntityHandler entityHandler = new EntityHandler();
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(entityHandler);
        ServerPlayConnectionEvents.JOIN.register(entityHandler);
        final BlockHandler blockHandler = new BlockHandler();
        PlayerBlockBreakEvents.AFTER.register(blockHandler);
        final HudRenderHandler hudRenderHandler = new HudRenderHandler();
        HudRenderCallback.EVENT.register(hudRenderHandler);

        ClientTickEvents.START_CLIENT_TICK.register((c) -> {
            while (IMPROVE_SCREEN_KEYBINDING.wasPressed())
                MinecraftClient.getInstance().setScreen(new ImproveScreen());
        });
    }

    @Override
    public PlayerData getPlayerData(LivingEntity player) throws RuntimeException {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER)
            throw new RuntimeException("You use serverbound api method in clientbound");
        return DataPersistentState.getPlayerData(player);
    }

    @Override
    public <D extends Data> D getData(Class<D> target) throws RuntimeException {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new RuntimeException("You use clientbound api method in serverbound");
        return DataProvider.get(target);
    }
}
