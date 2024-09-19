package ru.mentola.improvableskills.skill.attribute;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class NumberAttribute<T extends Number> extends Attribute<T> {
    public NumberAttribute(Identifier identifier, Text name, T minValue, T maxValue, T stepLevel) {
        super(identifier, minValue, maxValue, stepLevel, name, AttributeType.NUMBER, 1);
    }

    @Override
    public Attribute<T> copy() {
        NumberAttribute<T> numberAttribute = new NumberAttribute<>(this.getIdentifier(), this.getName(), this.getMinValue(), this.getMaxValue(), this.getStepLevel());
        numberAttribute.setLevel(this.getLevel());
        return numberAttribute;
    }
}