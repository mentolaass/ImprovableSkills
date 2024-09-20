package ru.mentola.improvableskills.skill.attribute;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class NumberAttribute<T extends Number> extends Attribute<T> {
    public NumberAttribute(Identifier identifier, Text name, int price, T minValue, T maxValue, T stepLevel) {
        super(identifier, minValue, maxValue, stepLevel, price, name, AttributeType.NUMBER, 1);
    }

    @Override
    public Attribute<T> copy(boolean saveLevel) {
        NumberAttribute<T> numberAttribute = new NumberAttribute<>(this.getIdentifier(), this.getName(), this.getPrice(), this.getMinValue(), this.getMaxValue(), this.getStepLevel());
        if (saveLevel) numberAttribute.setLevel(this.getLevel());
        return numberAttribute;
    }
}