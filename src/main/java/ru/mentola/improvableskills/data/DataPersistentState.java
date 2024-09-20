package ru.mentola.improvableskills.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import ru.mentola.improvableskills.ImprovableSkills;
import ru.mentola.improvableskills.skill.Skill;
import ru.mentola.improvableskills.skill.provider.SkillProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public final class DataPersistentState extends PersistentState {
    private HashMap<UUID, PlayerData> playerData = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound compound = new NbtCompound();
        playerData.forEach((player, data) -> compound.put(player.toString(), data.asNbt()));
        nbt.put("players", compound);
        return nbt;
    }

    public static DataPersistentState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        DataPersistentState state = new DataPersistentState();
        NbtCompound compound = nbt.getCompound("players");
        compound.getKeys().forEach((player) -> {
            NbtCompound playerCompound = compound.getCompound(player);
            PlayerData data = new PlayerData();
            data.setPoints(playerCompound.getInt("points"));
            data.setLevel(playerCompound.getInt("level"));
            NbtList skills = playerCompound.getList("skills", NbtElement.COMPOUND_TYPE);
            Set<Skill> skillSet = new HashSet<>();
            skills.forEach((skill) -> {
                NbtCompound skillCompound = (NbtCompound) skill;
                skillSet.add(SkillProvider.fromNbt(skillCompound));
            });
            data.setSkillSet(skillSet);
            state.getPlayerData().put(UUID.fromString(player), data);
        });
        return state;
    }

    private static Type<DataPersistentState> type = new Type<>(
            DataPersistentState::new,
            DataPersistentState::fromNbt,
            null);

    public static DataPersistentState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        DataPersistentState state = persistentStateManager.getOrCreate(type, ImprovableSkills.MOD_ID);
        state.markDirty();
        return state;
    }

    public static PlayerData getPlayerData(LivingEntity player) {
        DataPersistentState state = getServerState(player.getWorld().getServer());
        return state.getPlayerData().computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }
}