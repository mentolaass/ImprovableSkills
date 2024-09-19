package ru.mentola.improvableskills.skill.provider;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.attribute.Attribute;
import ru.mentola.improvableskills.skill.attribute.Attributes;

public final class AttributeProvider {
    public AttributeProvider() {
        throw new RuntimeException("Unsupported");
    }

    @Nullable
    public static Attribute<?> getById(Identifier id) {
        if (id.equals(Constants.MINER_LUCK_ATTRIBUTE_PERCENT))
            return Attributes.MINER_LUCK_ATTRIBUTE_PERCENT.copy();
        if (id.equals(Constants.MINER_LUCK_ATTRIBUTE_COUNT))
            return Attributes.MINER_LUCK_ATTRIBUTE_COUNT.copy();
        if (id.equals(Constants.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH))
            return Attributes.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH.copy();
        if (id.equals(Constants.VAMPIRISM_ATTRIBUTE_CHANCE))
            return Attributes.VAMPIRISM_ATTRIBUTE_CHANCE.copy();
        return null;
    }

    @Nullable
    public static Attribute<?> fromNbt(NbtCompound nbtCompound) {
        Identifier identifier = Identifier.of(nbtCompound.getString("id"));
        int level = nbtCompound.getInt("level");
        Attribute<?> attribute = AttributeProvider.getById(identifier);
        if (attribute == null) return null;
        attribute.setLevel(level);
        return attribute;
    }
}