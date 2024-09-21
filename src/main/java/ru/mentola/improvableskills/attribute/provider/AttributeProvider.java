package ru.mentola.improvableskills.attribute.provider;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.attribute.Attribute;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class AttributeProvider {
    private static final Set<Attribute<?>> attributes = new LinkedHashSet<>();

    public AttributeProvider() {
        throw new RuntimeException("Unsupported");
    }

    @Nullable
    public static Attribute<?> getById(Identifier id) {
        Attribute<?> tempAttribute = attributes.stream()
                .filter((attribute) -> attribute.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
        if (tempAttribute == null) return null;
        return tempAttribute.copy(false);
    }

    public static <T extends Attribute<?>> void registerAttribute(T attribute) {
        attributes.add(attribute.copy(false));
    }

    public static void unregisterAttribute(Identifier id) {
        attributes.removeIf((attribute) -> attribute.getIdentifier().equals(id));
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

    public static Set<Attribute<?>> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }
}