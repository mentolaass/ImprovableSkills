package ru.mentola.improvableskills.attribute.factory;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.attribute.NumberAttribute;

public final class AttributeFactory {
    public static <T extends Number> NumberAttribute<T> createNumAttribute(String identifier, String name, String desc, int price, T minValue, T maxValue, T stepLevel) {
        return new NumberAttribute<T>(Identifier.of(identifier), Text.of(name), Text.of(desc), price, minValue, maxValue, stepLevel);
    }
}