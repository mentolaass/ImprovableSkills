package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class MerchantHandSkill extends Skill {
    public MerchantHandSkill() {
        super(
                Constants.MERCHANTHAND_SKILL,
                Constants.MERCHANTHAND_SKILL_TEX,
                Text.translatable("skill.improvableskills.merchanthand"),
                Text.translatable("skill.improvableskills.merchanthand.desc"),
                100000,
                20,
                Set.of(

                )
        );
    }
}
