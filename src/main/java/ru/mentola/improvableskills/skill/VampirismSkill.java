package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.attribute.Attributes;
import ru.mentola.improvableskills.skill.base.Skill;

import java.util.Set;

public final class VampirismSkill extends Skill {
    public VampirismSkill() {
        super(
                Constants.VAMPIRISM_SKILL,
                Text.translatable("skill.improvableskills.vampirism"),
                Text.translatable("skill.improvableskills.vampirism.desc"),
                5000,
                1,
                Set.of(Attributes.VAMPIRISM_ATTRIBUTE_CHANCE.copy(),
                        Attributes.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH.copy())
        );
    }
}