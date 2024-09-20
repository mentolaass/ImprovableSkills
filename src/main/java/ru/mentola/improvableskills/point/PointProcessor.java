package ru.mentola.improvableskills.point;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;

@UtilityClass
public final class PointProcessor {
    public int get(PointType type, Object... params) {
        return switch (type) {
            case KILL_ENTITY -> getByKillEntity((LivingEntity) params[0]);
        };
    }

    private int getByKillEntity(LivingEntity entity) {
        int basePoints = 50;
        double healthFactor = entity.getMaxHealth() / 20.0;
        double rarityFactor = getRarityFactor(entity);
        int points = (int) (basePoints * healthFactor * rarityFactor);
        return Math.min(points, 500);
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