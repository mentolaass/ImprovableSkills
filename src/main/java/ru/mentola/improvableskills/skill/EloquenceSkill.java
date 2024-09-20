package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class EloquenceSkill extends Skill {
    public EloquenceSkill() {
        super(
                Constants.ELOQUENCE_SKILL,
                Constants.ELOQUENCE_SKILL_TEX,
                Text.translatable("skill.improvableskills.eloquence"),
                Text.translatable("skill.improvableskills.eloquence.desc"),
                90000,
                17,
                Set.of(

                ));
    }
}
