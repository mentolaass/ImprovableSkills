package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class FishBreathSkill extends Skill {
    public FishBreathSkill() {
        super(
                Constants.FISHBREATH_SKILL,
                Constants.FISHBREATH_SKILL_TEX,
                Text.translatable("skill.improvableskills.fishbreath"),
                Text.translatable("skill.improvableskills.fishbreath.desc"),
                30000,
                6,
                Set.of(

                ));
    }
}
