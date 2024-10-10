package ru.mentola.improvableskills;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.mentola.improvableskills.api.event.manager.EventManager;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.handler.BlockHandler;
import ru.mentola.improvableskills.handler.EntityHandler;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.NetworkHandler;
import ru.mentola.improvableskills.network.payload.*;
import ru.mentola.improvableskills.network.payload.side.Side;
import ru.mentola.improvableskills.skill.*;
import ru.mentola.improvableskills.attribute.provider.AttributeProvider;
import ru.mentola.improvableskills.skill.provider.SkillProvider;
import ru.mentola.improvableskills.client.sound.CustomSound;

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

        Network.registerServerReceiver(PumpSkillPayload.PAYLOAD_ID, NetworkHandler::handlePumpSkill);
        Network.registerServerReceiver(PumpLevelPayload.PAYLOAD_ID, NetworkHandler::handlePumpLevel);
        Network.registerServerReceiver(PumpSkillAttributePayload.PAYLOAD_ID, NetworkHandler::handlePumpSkillAttribute);

        final EntityHandler entityHandler = new EntityHandler();
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(entityHandler);
        ServerPlayConnectionEvents.JOIN.register(entityHandler);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(entityHandler);
        final BlockHandler blockHandler = new BlockHandler();
        PlayerBlockBreakEvents.AFTER.register(blockHandler);
    }
}
