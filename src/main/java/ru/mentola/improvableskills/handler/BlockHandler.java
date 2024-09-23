package ru.mentola.improvableskills.handler;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.attribute.NumberAttribute;
import ru.mentola.improvableskills.data.DataPersistentState;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.network.Network;
import ru.mentola.improvableskills.network.payload.DataUpdatePayload;
import ru.mentola.improvableskills.network.payload.NoticePayload;
import ru.mentola.improvableskills.point.PointProcessor;
import ru.mentola.improvableskills.point.PointType;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.util.Util;

import java.util.List;

public final class BlockHandler implements
        PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            PlayerData playerData = DataPersistentState.getPlayerData(serverPlayer);
            int points = PointProcessor.get(PointType.BREAK_BLOCK, state.getBlock());
            if (points > 0) {
                playerData.setPoints(playerData.getPoints() + points);
                Network.sendTo(serverPlayer, new DataUpdatePayload(playerData));
                Network.sendTo(serverPlayer, new NoticePayload(String.format("+ %s очков", points), Constants.POINTS_TEX));
            }
            if (playerData.containsSkill(Constants.MINER_LUCK_SKILL)) {
                Skill skill = playerData.getSkill(Constants.MINER_LUCK_SKILL);
                if (skill.containsAttribute(Constants.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL)
                    && skill.containsAttribute(Constants.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP)) {
                    NumberAttribute<?> chanceCallAttribute = (NumberAttribute<?>) skill.getAttribute(ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL.getIdentifier());
                    NumberAttribute<?> countDropAttribute = (NumberAttribute<?>) skill.getAttribute(ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP.getIdentifier());
                    int chanceCall = Util.getNumberValueAttribute(chanceCallAttribute).intValue();
                    int countDrop = Util.getNumberValueAttribute(countDropAttribute).intValue();
                    if (chanceCall >= Util.randomNumber(100, 0)) {
                        List<Item> itemDrop = Util.getOreDrop(state.getBlock(), countDrop);
                        itemDrop.forEach((item) -> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item))));
                    }
                }
            }
        }
    }
}