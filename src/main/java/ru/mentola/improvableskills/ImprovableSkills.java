package ru.mentola.improvableskills;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.mentola.improvableskills.api.ImprovableSkillsAPI;
import ru.mentola.improvableskills.api.provider.ImprovableSkillsProvider;
import ru.mentola.improvableskills.client.screen.ImproveScreen;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.client.handler.HudRenderHandler;
import ru.mentola.improvableskills.handler.BlockHandler;
import ru.mentola.improvableskills.handler.EntityHandler;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.*;
import ru.mentola.improvableskills.network.payload.side.Side;
import ru.mentola.improvableskills.client.notice.Notice;
import ru.mentola.improvableskills.client.notice.NoticeQueue;
import ru.mentola.improvableskills.skill.*;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.attribute.Attributes;
import ru.mentola.improvableskills.attribute.provider.AttributeProvider;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.client.sound.CustomSound;
import ru.mentola.improvableskills.util.Util;

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
        CustomSound.initialize();

        Network.registerPayload(PumpSkillPayload.PAYLOAD_ID, PumpSkillPayload.PAYLOAD_PACKET_CODEC, Side.CLIENT);
        Network.registerPayload(PumpLevelPayload.PAYLOAD_ID, PumpLevelPayload.PAYLOAD_PACKET_CODEC, Side.CLIENT);
        Network.registerPayload(PumpSkillAttributePayload.PAYLOAD_ID, PumpSkillAttributePayload.PAYLOAD_PACKET_CODEC, Side.CLIENT);
        Network.registerPayload(DataUpdatePayload.PAYLOAD_ID, DataUpdatePayload.PAYLOAD_PACKET_CODEC, Side.SERVER);
        Network.registerPayload(NoticePayload.PAYLOAD_ID, NoticePayload.PAYLOAD_PACKET_CODEC, Side.SERVER);

        SkillProvider.registerSkill(new MinerLuckSkill());
        SkillProvider.registerSkill(new VampirismSkill());
        SkillProvider.registerSkill(new RabbitSkill());
        SkillProvider.registerSkill(new MasterWeaponSkill());
        SkillProvider.registerSkill(new CreedSkill());
        SkillProvider.registerSkill(new BeholderSkill());
        SkillProvider.registerSkill(new AikidoSkill());
        SkillProvider.registerSkill(new FarmerSkill());
        SkillProvider.registerSkill(new WoodCutterSkill());
        SkillProvider.registerSkill(new FishBreathSkill());
        SkillProvider.registerSkill(new HardnessSkill());
        SkillProvider.registerSkill(new MerchantHandSkill());
        SkillProvider.registerSkill(new EloquenceSkill());
        SkillProvider.registerSkill(new StarNavigatorSkill());

        AttributeProvider.registerAttribute(Attributes.MINER_LUCK_ATTRIBUTE_COUNT);
        AttributeProvider.registerAttribute(Attributes.MINER_LUCK_ATTRIBUTE_PERCENT);
        AttributeProvider.registerAttribute(Attributes.VAMPIRISM_ATTRIBUTE_CHANCE);
        AttributeProvider.registerAttribute(Attributes.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH);
        AttributeProvider.registerAttribute(Attributes.RABBIT_ATTRIBUTE_POWER_JUMP);
        AttributeProvider.registerAttribute(Attributes.RABBIT_ATTRIBUTE_SPEED);
        AttributeProvider.registerAttribute(Attributes.MASTER_WEAPON_ATTRIBUTE_DAMAGE);

        ImprovableSkillsProvider.setAPI(this);
    }

    @Override
    public void onInitializeClient() {
        Network.registerClientReceiver(DataUpdatePayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataProvider.get(PlayerData.class);
            if (playerData == null) throw new RuntimeException("Client Player Data Is Null");
            playerData.copyOf(payload.getPlayerData());
        });

        Network.registerClientReceiver(NoticePayload.PAYLOAD_ID, (payload, context) -> NoticeQueue.addToQueue(new Notice(payload.getText(), payload.getIdentifier())));

        Network.registerServerReceiver(PumpSkillPayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            if (playerData.containsSkill(payload.getIdSkill())) return;
            Skill skill = SkillProvider.getById(payload.getIdSkill());
            if (skill == null) return;
            if (playerData.getPoints() < skill.getPricePoints()
                    || playerData.getLevel() < skill.getNeedLevel()) return;
            playerData.setPoints(playerData.getPoints() - skill.getPricePoints());
            playerData.attachSkill(skill.copy());
            Network.sendTo(context.player(), new DataUpdatePayload(playerData));
        });

        Network.registerServerReceiver(PumpLevelPayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            int need = Util.getNextPointsToNextLevelNeed(playerData);
            if (playerData.getPoints() < need) return;
            playerData.setPoints(playerData.getPoints() - need);
            playerData.setLevel(playerData.getLevel() + 1);
            Network.sendTo(context.player(), new DataUpdatePayload(playerData));
        });

        Network.registerServerReceiver(PumpSkillAttributePayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            if (!playerData.containsSkill(payload.getIdSkill())) return;
            Skill skill = playerData.getSkill(payload.getIdSkill());
            if (skill == null) return;
            if (!skill.containsAttribute(payload.getIdAttribute())) return;
            int price = skill.getAttribute(payload.getIdAttribute()).getPrice();
            if (playerData.getPoints() < price) return;
            if (skill.upgradeAttribute(payload.getIdAttribute())) {
                playerData.setPoints(playerData.getPoints() - price);
                Network.sendTo(context.player(), new DataUpdatePayload(playerData));
            }
        });

        // register common events
        final EntityHandler entityHandler = new EntityHandler();
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(entityHandler);
        ServerPlayConnectionEvents.JOIN.register(entityHandler);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(entityHandler);
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

    @Override
    public void sendNotice(Notice notice) throws RuntimeException {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
            throw new RuntimeException("You use clientbound api method in serverbound");
        NoticeQueue.addToQueue(notice);
    }

    @Override
    public void registerSkill(Skill skill) {
        SkillProvider.registerSkill(skill);
    }

    @Override
    public void unregisterSkill(Identifier id) {
        SkillProvider.unregisterSkill(id);
    }

    @Override
    public void registerAttribute(Attribute<?> attribute) {
        AttributeProvider.registerAttribute(attribute);
    }

    @Override
    public void unregisterAttribute(Identifier id) {
        AttributeProvider.unregisterAttribute(id);
    }
}
