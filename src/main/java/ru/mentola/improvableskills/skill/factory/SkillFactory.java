package ru.mentola.improvableskills.skill.factory;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.skill.Skill;

import java.util.Set;

public final class SkillFactory {
    public static Skill createNew(Identifier id, Identifier tex, Text name, Text description, int pricePoints, int needLevel, Set<Attribute<?>> attributes) {
        return new Skill(id, tex, name, description, pricePoints, needLevel, attributes);
    }
}