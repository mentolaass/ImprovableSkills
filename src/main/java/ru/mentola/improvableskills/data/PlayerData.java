package ru.mentola.improvableskills.data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.skill.Skill;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public final class PlayerData implements Data {
    @SerializedName("points")
    private int points = 10000000;
    @SerializedName("level")
    private int level = 200;
    @SerializedName("skills")
    private Set<Skill> skillSet = new HashSet<>();

    public void copyOf(PlayerData playerData) {
        this.points = playerData.getPoints();
        this.level = playerData.getLevel();
        this.skillSet = playerData.getSkillSet();
    }

    public boolean containsSkill(Identifier identifier) {
        return getSkill(identifier) != null;
    }

    @Nullable
    public Skill getSkill(Identifier identifier) {
        return skillSet.stream()
                .filter((skill) -> skill.getId().equals(identifier))
                .findFirst()
                .orElse(null);
    }

    public void attachSkill(Skill skill) {
        skillSet.add(skill);
    }

    public void detachSkill(Skill skill) {
        skillSet.removeIf((s) -> s.getId().equals(skill.getId()));
    }

    @Override
    public String toString() {
        return String.format("PlayerData{level=%s,points=%s,skills=%s}", this.level, this.points,
                String.join(",", this.skillSet.stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet())));
    }

    @Override
    public NbtCompound asNbt() {
        NbtCompound playerCompound = new NbtCompound();
        playerCompound.putInt("points", this.getPoints());
        playerCompound.putInt("level", this.getLevel());
        NbtList skills = new NbtList();
        this.getSkillSet().forEach((skill) -> {
            if (skill != null) skills.add(skill.asNbt());
        });
        playerCompound.put("skills", skills);
        return playerCompound;
    }

    public String toJson() {
        return new GsonBuilder()
                .registerTypeAdapter(Attribute.class, new Attribute.Serializer())
                .registerTypeAdapter(Skill.class, new Skill.Serializer())
                .create().toJson(this);
    }

    public static PlayerData fromJson(String raw) {
        return new GsonBuilder()
                .registerTypeAdapter(Attribute.class, new Attribute.Deserializer())
                .registerTypeAdapter(Skill.class, new Skill.Deserializer())
                .create().fromJson(raw, PlayerData.class);
    }
}