package ru.mentola.improvableskills.attribute.factory;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.attribute.NumberAttribute;

public final class AttributeFactory {
    public static <T extends Number> NumberAttribute<T> createNumAttribute(Identifier identifier, Text name, Text desc, int price, T minValue, T maxValue, T stepLevel) {
        return new NumberAttribute<T>(identifier, name, desc, price, minValue, maxValue, stepLevel);
    }
}