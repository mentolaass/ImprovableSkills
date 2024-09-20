package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class StarNavigatorSkill extends Skill {
    public StarNavigatorSkill() {
        super(
                Constants.STARNAVIGATOR_SKILL,
                Constants.STARNAVIGATOR_SKILL_TEX,
                Text.translatable("skill.improvableskills.starnavigator"),
                Text.translatable("skill.improvableskills.starnavigator.desc"),
                45111,
                8,
                Set.of(

                ));
    }
}