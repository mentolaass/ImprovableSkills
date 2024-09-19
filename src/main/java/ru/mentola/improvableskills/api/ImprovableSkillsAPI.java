package ru.mentola.improvableskills.api;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.api.side.SideAPI;
import ru.mentola.improvableskills.api.side.SidedAPI;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.data.PlayerData;

public interface ImprovableSkillsAPI {
    /**
     * @throws RuntimeException invalid side
     */
    @SidedAPI(side=SideAPI.SERVER)
    PlayerData getPlayerData(LivingEntity player) throws RuntimeException;
    /**
     * @param target any class extend of {@link Data}
     * @throws RuntimeException invalid side
     */
    @SidedAPI(side=SideAPI.CLIENT)
    <D extends Data> @Nullable D getData(Class<D> target) throws RuntimeException;
}