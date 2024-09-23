package ru.mentola.improvableskills.point;

import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import ru.mentola.improvableskills.ImprovableSkills;
import ru.mentola.improvableskills.api.event.Listener;
import ru.mentola.improvableskills.api.event.v1.PlayerGetPointsEvent;
import ru.mentola.improvableskills.util.Util;

@UtilityClass
public final class PointProcessor {
    public int get(PointType type, Object... params) {
        int points = switch (type) {
            case KILL_ENTITY -> getByKillEntity((LivingEntity) params[0]);
            case BREAK_BLOCK -> getByBreakBlock((Block) params[0]);
        };
        PlayerGetPointsEvent playerGetPointsEvent = new PlayerGetPointsEvent(points, type);
        for (Listener listener : ImprovableSkills.EVENT_MANAGER.getRegisteredListeners())
            if (listener instanceof PlayerGetPointsEvent.Subscribe eventSub)
                eventSub.onPlayerGetPoints(playerGetPointsEvent);
        return playerGetPointsEvent.isCancelled() ? 0 : points;
    }

    private int getByBreakBlock(Block block) {
        return Util.isOre(block) ? 20 : 5;
    }

    private int getByKillEntity(LivingEntity entity) {
        int basePoints = 50;
        double healthFactor = entity.getMaxHealth() / 20.0;
        double rarityFactor = getRarityFactor(entity);
        int points = (int) (basePoints * healthFactor * rarityFactor);
        return Math.min(points, 2500);
    }

    private double getRarityFactor(Entity entity) {
        if (entity instanceof EnderDragonEntity || entity instanceof WitherEntity) {
            return 5.0;
        } else if (entity instanceof VillagerEntity || entity instanceof IronGolemEntity) {
            return 2.0;
        } else {
            return 1.0;
        }
    }
}