package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class CreedSkill extends Skill {
    public CreedSkill() {
        super(
                Constants.CREED_SKILL,
                Constants.CREED_SKILL_TEX,
                Text.translatable("skill.improvableskills.creed"),
                Text.translatable("skill.improvableskills.creed.desc"),
                90000,
                15,
                Set.of(

                ));
    }
}