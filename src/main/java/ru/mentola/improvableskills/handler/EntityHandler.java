package ru.mentola.improvableskills.handler;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.NoticePayload;
import ru.mentola.improvableskills.point.PointProcessor;
import ru.mentola.improvableskills.point.PointType;
import ru.mentola.improvableskills.shared.Constants;

public final class EntityHandler implements
        ServerEntityCombatEvents.AfterKilledOtherEntity,
        ServerLivingEntityEvents.AllowDamage,
        ServerPlayConnectionEvents.Join {
    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (entity instanceof ServerPlayerEntity player) {
            int points = PointProcessor.get(PointType.KILL_ENTITY, killedEntity);
            if (points > 0) {
                PlayerData playerData = DataPersistentState.getPlayerData(player);
                playerData.setPoints(playerData.getPoints() + points);
                Network.sendTo(player, new DataUpdatePayload(playerData));
                Network.sendTo(player, new NoticePayload(String.format("+%s очков", points), Constants.POINTS_TEX));
            }
        }
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        PlayerData playerData = DataPersistentState.getPlayerData(handler.player);
        Network.sendTo(handler.player, new DataUpdatePayload(playerData));
    }

    @Override
    public boolean allowDamage(LivingEntity entity, DamageSource source, float amount) {
        return true;
    }
}
