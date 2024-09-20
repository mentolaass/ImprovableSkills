package ru.mentola.improvableskills.skill.provider;

import lombok.Getter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.skill.base.Skill;
import ru.mentola.improvableskills.skill.attribute.Attribute;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class SkillProvider {
    private static final Set<Skill> skills = new HashSet<>();

    public SkillProvider() {
        throw new RuntimeException("Unsupported");
    }

    @Nullable
    public static Skill getById(Identifier id) {
        Skill tempSkill = skills.stream()
                .filter((skill) -> skill.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (tempSkill == null) return null;
        return tempSkill.copy();
    }

    public static void registerSkill(Skill skill) {
        skills.add(skill);
    }

    public static void unregisterSkill(Identifier id) {
        skills.removeIf((skill) -> skill.getId().equals(id));
    }

    @Nullable
    public static Skill fromNbt(NbtCompound nbtCompound) {
        Identifier identifier = Identifier.of(nbtCompound.getString("id"));
        Skill skill = SkillProvider.getById(identifier);
        if (skill == null) return null;
        NbtList attributesList = nbtCompound.getList("attributes", NbtElement.COMPOUND_TYPE);
        Set<Attribute<?>> attributeSet = new HashSet<>();
        attributesList.forEach((attribute) -> attributeSet.add(AttributeProvider.fromNbt((NbtCompound) attribute)));
        skill.setAttributes(attributeSet);
        return skill;
    }

    public static Set<Skill> getSkills() {
        return Collections.unmodifiableSet(skills);
    }
}