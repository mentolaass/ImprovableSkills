package ru.mentola.improvableskills.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import ru.mentola.improvableskills.ImprovableSkills;
import ru.mentola.improvableskills.api.event.Listener;
import ru.mentola.improvableskills.api.event.v1.PlayerDataUpdateEvent;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.PumpLevelPayload;
import ru.mentola.improvableskills.network.payload.PumpSkillAttributePayload;
import ru.mentola.improvableskills.network.payload.PumpSkillPayload;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.util.Util;

public final class NetworkHandler {
    public static void handlePumpSkill(PumpSkillPayload payload, ServerPlayNetworking.Context context) {
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
    }

    public static void handlePumpLevel(PumpLevelPayload payload, ServerPlayNetworking.Context context) {
        PlayerData playerData = DataPersistentState.getPlayerData(context.player());
        int need = Util.getNextPointsToNextLevelNeed(playerData);
        if (playerData.getPoints() < need) return;
        if (isCancelledWithUpdateEvent(playerData, new PlayerDataUpdateEvent.UpdateData(null, null, null, playerData.getLevel() + 1), PlayerDataUpdateEvent.Type.UPGRADE_LEVEL)) return;
        playerData.setPoints(playerData.getPoints() - need);
        playerData.setLevel(playerData.getLevel() + 1);
        Network.sendTo(context.player(), new DataUpdatePayload(playerData));
    }

    public static void handlePumpSkillAttribute(PumpSkillAttributePayload payload, ServerPlayNetworking.Context context) {
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
    }

    private static boolean isCancelledWithUpdateEvent(PlayerData playerData, PlayerDataUpdateEvent.UpdateData updateData, PlayerDataUpdateEvent.Type type) {
        PlayerDataUpdateEvent event = new PlayerDataUpdateEvent(playerData, type, updateData);
        for (Listener listener : ImprovableSkills.EVENT_MANAGER.getRegisteredListeners())
            if (listener instanceof PlayerDataUpdateEvent.Subscribe eventSub)
                eventSub.onPlayerDataUpdate(event);
        return event.isCancelled();
    }
}