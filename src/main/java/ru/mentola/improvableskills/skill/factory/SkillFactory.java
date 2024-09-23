package ru.mentola.improvableskills.skill.factory;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.skill.Skill;

import java.util.Set;

public final class SkillFactory {
    public static Skill createNew(String id, String tex, String name, String description, int pricePoints, int needLevel, Set<Attribute<?>> attributes) {
        return new Skill(Identifier.of(id), Identifier.of(tex), Text.of(name), Text.of(description), pricePoints, needLevel, attributes);
    }
}