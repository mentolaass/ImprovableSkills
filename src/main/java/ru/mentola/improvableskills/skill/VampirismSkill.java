package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class VampirismSkill extends Skill {
    public VampirismSkill() {
        super(
                Constants.VAMPIRISM_SKILL,
                Constants.VAMPIRISM_SKILL_TEX,
                Text.translatable("skill.improvableskills.vampirism"),
                Text.translatable("skill.improvableskills.vampirism.desc"),
                5000,
                3,
                Set.of(

                )
        );
    }
}