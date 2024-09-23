package ru.mentola.improvableskills.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.api.event.Listener;
import ru.mentola.improvableskills.api.side.SideAPI;
import ru.mentola.improvableskills.api.side.SidedAPI;
import ru.mentola.improvableskills.attribute.provider.AttributeProvider;
import ru.mentola.improvableskills.client.data.DataProvider;
import ru.mentola.improvableskills.client.notice.Notice;
import ru.mentola.improvableskills.client.notice.NoticeQueue;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.NoticePayload;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.skill.provider.SkillProvider;

import static ru.mentola.improvableskills.ImprovableSkills.EVENT_MANAGER;

public final class ImprovableSkillsAPI {
    @SidedAPI(side=SideAPI.SERVER)
    public static PlayerData getPlayerData(ServerPlayerEntity player) {
        return DataPersistentState.getPlayerData(player);
    }

    /**
     * @param target any class extend of {@link Data}
     */
    @SidedAPI(side=SideAPI.CLIENT)
    public static <D extends Data> @Nullable D getData(Class<D> target) {
        return DataProvider.get(target);
    }

    @SidedAPI(side=SideAPI.SERVER)
    public static void sendServerNotice(ServerPlayerEntity player, Notice notice) {
        Network.sendTo(player, new NoticePayload(notice.getText(), notice.getTextureId()));
    }

    @SidedAPI(side=SideAPI.SERVER)
    public static void registerListener(Listener listener) {
        EVENT_MANAGER.registerListener(listener);
    }

    @SidedAPI(side=SideAPI.SERVER)
    public static void unregisterListener(Listener listener) {
        EVENT_MANAGER.unregisterListener(listener);
    }

    @SidedAPI(side=SideAPI.CLIENT)
    public static void sendClientNotice(Notice notice) {
        NoticeQueue.addToQueue(notice);
    }

    @SidedAPI(side=SideAPI.BOTH)
    public static void registerSkill(Skill skill) {
        SkillProvider.registerSkill(skill);
    }

    @SidedAPI(side=SideAPI.BOTH)
    public static void unregisterSkill(Identifier id) {
        SkillProvider.unregisterSkill(id);
    }

    @SidedAPI(side=SideAPI.BOTH)
    public static void registerAttribute(Attribute<?> attribute) {
        AttributeProvider.registerAttribute(attribute);
    }

    @SidedAPI(side=SideAPI.BOTH)
    public static void unregisterAttribute(Identifier id) {
        AttributeProvider.unregisterAttribute(id);
    }
}