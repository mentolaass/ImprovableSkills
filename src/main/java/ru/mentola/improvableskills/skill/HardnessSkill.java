package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class HardnessSkill extends Skill {
    public HardnessSkill() {
        super(
                Constants.HARDNESS_SKILL,
                Constants.HARDNESS_SKILL_TEX,
                Text.translatable("skill.improvableskills.hardness"),
                Text.translatable("skill.improvableskills.hardness.desc"),
                22222,
                4,
                Set.of(

                ));
    }
}