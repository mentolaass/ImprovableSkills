package ru.mentola.improvableskills.point;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;

@UtilityClass
public final class PointProcessor {
    public int get(PointType type, Object... params) {
        return switch (type) {
            case KILL_ENTITY -> getByKillEntity((Entity)params[0]);
        };
    }

    private int getByKillEntity(Entity entity) {
        return 1500;
    }
}