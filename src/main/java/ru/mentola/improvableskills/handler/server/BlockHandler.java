package ru.mentola.improvableskills.handler.server;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.data.server.DataPersistentState;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.base.Skill;
import ru.mentola.improvableskills.skill.attribute.NumberAttribute;
import ru.mentola.improvableskills.util.Util;

import java.util.Set;

public final class BlockHandler
        implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        PlayerData playerData = DataPersistentState.getPlayerData(player);
        if (playerData.containsSkill(Constants.MINER_LUCK_SKILL)) {
            Skill skill = playerData.getSkill(Constants.MINER_LUCK_SKILL);
            NumberAttribute<Integer> percent = (NumberAttribute<Integer>) skill.getAttribute(Constants.MINER_LUCK_ATTRIBUTE_PERCENT);
            NumberAttribute<Integer> count = (NumberAttribute<Integer>) skill.getAttribute(Constants.MINER_LUCK_ATTRIBUTE_COUNT);
            int valuePercent = Util.getNumberValueAttribute(percent);
            int valueCount = Util.getNumberValueAttribute(count);
            if ((double) valuePercent / 100 >= Math.random()) {
                Set<Item> drop = Util.getOreDrop(state.getBlock(), valueCount);
                drop.forEach((item) -> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item))));
            }
        }
    }
}