package ru.mentola.improvableskills.skill.provider;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.MinerLuckSkill;
import ru.mentola.improvableskills.skill.VampirismSkill;
import ru.mentola.improvableskills.skill.base.Skill;
import ru.mentola.improvableskills.skill.attribute.Attribute;

import java.util.HashSet;
import java.util.Set;

public final class SkillProvider {
    public SkillProvider() {
        throw new RuntimeException("Unsupported");
    }

    @Nullable
    public static Skill getById(Identifier id) {
        if (id.equals(Constants.MINER_LUCK_SKILL))
            return new MinerLuckSkill();
        if (id.equals(Constants.VAMPIRISM_SKILL))
            return new VampirismSkill();
        return null;
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
}