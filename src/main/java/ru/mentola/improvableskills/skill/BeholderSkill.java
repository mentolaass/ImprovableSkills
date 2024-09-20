package ru.mentola.improvableskills.skill;


import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class BeholderSkill extends Skill {
    public BeholderSkill() {
        super(
                Constants.BEHOLDER_SKILL,
                Constants.BEHOLDER_SKILL_TEX,
                Text.translatable("skill.improvableskills.beholder"),
                Text.translatable("skill.improvableskills.beholder.desc"),
                60000,
                10,
                Set.of(

                ));
    }
}
