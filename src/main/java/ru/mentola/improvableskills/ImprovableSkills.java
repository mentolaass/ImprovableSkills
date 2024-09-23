package ru.mentola.improvableskills;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.mentola.improvableskills.api.event.Listener;
import ru.mentola.improvableskills.api.event.manager.EventManager;
import ru.mentola.improvableskills.api.event.v1.PlayerDataUpdateEvent;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.handler.BlockHandler;
import ru.mentola.improvableskills.handler.EntityHandler;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.*;
import ru.mentola.improvableskills.network.payload.side.Side;
import ru.mentola.improvableskills.skill.*;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.attribute.provider.AttributeProvider;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.client.sound.CustomSound;
import ru.mentola.improvableskills.util.Util;

public final class ImprovableSkills implements ModInitializer {
    public static final String MOD_ID = "improvableskills";

    public static final EventManager EVENT_MANAGER = new EventManager();

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

        AttributeProvider.registerAttribute(ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL);
        AttributeProvider.registerAttribute(ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP);
        AttributeProvider.registerAttribute(ModAttributes.VAMPIRISM_SKILL_ATTRIBUTE_CHANCE_CALL);
        AttributeProvider.registerAttribute(ModAttributes.VAMPIRISM_SKILL_ATTRIBUTE_COUNT_RETURN);

        Network.registerServerReceiver(PumpSkillPayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            if (playerData.containsSkill(payload.getIdSkill())) return;
            Skill skill = SkillProvider.getById(payload.getIdSkill());
            if (skill == null) return;
            if (playerData.getPoints() < skill.getPricePoints()
                    || playerData.getLevel() < skill.getNeedLevel()) return;
            if (isCancelledWithUpdateEvent(playerData, new PlayerDataUpdateEvent.UpdateData(null, null, skill, -1), PlayerDataUpdateEvent.Type.ATTACH_SKILL)) return;
            playerData.setPoints(playerData.getPoints() - skill.getPricePoints());
            playerData.attachSkill(skill.copy());
            Network.sendTo(context.player(), new DataUpdatePayload(playerData));
        });

        Network.registerServerReceiver(PumpLevelPayload.PAYLOAD_ID, (payload, context) -> {
            PlayerData playerData = DataPersistentState.getPlayerData(context.player());
            int need = Util.getNextPointsToNextLevelNeed(playerData);
            if (playerData.getPoints() < need) return;
            if (isCancelledWithUpdateEvent(playerData, new PlayerDataUpdateEvent.UpdateData(null, null, null, playerData.getLevel() + 1), PlayerDataUpdateEvent.Type.UPGRADE_LEVEL)) return;
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
            Attribute<?> attribute = skill.getAttribute(payload.getIdAttribute());
            int price = attribute.getPrice() * payload.getCount();
            if (playerData.getPoints() < price) return;
            if (isCancelledWithUpdateEvent(playerData, new PlayerDataUpdateEvent.UpdateData(attribute, skill, null, -1), PlayerDataUpdateEvent.Type.UPGRADE_ATTRIBUTE)) return;
            if (skill.upgradeAttribute(payload.getIdAttribute(), payload.getCount())) {
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
    }

    private boolean isCancelledWithUpdateEvent(PlayerData playerData, PlayerDataUpdateEvent.UpdateData updateData, PlayerDataUpdateEvent.Type type) {
        PlayerDataUpdateEvent event = new PlayerDataUpdateEvent(playerData, type, updateData);
        for (Listener listener : EVENT_MANAGER.getRegisteredListeners())
            if (listener instanceof PlayerDataUpdateEvent.Subscribe eventSub)
                eventSub.onPlayerDataUpdate(event);
        return event.isCancelled();
    }
}
