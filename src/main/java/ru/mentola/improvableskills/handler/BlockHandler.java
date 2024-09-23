package ru.mentola.improvableskills.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.NoticePayload;
import ru.mentola.improvableskills.point.PointProcessor;
import ru.mentola.improvableskills.point.PointType;
import ru.mentola.improvableskills.shared.Constants;

public final class BlockHandler implements
        PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            int points = PointProcessor.get(PointType.BREAK_BLOCK, state.getBlock());
            if (points > 0) {
                PlayerData playerData = DataPersistentState.getPlayerData(serverPlayer);
                playerData.setPoints(playerData.getPoints() + points);
                Network.sendTo(serverPlayer, new DataUpdatePayload(playerData));
                Network.sendTo(serverPlayer, new NoticePayload(String.format("+ %s очков", points), Constants.POINTS_TEX));
            }
        }
    }
}