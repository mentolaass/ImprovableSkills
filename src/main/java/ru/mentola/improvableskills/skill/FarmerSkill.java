package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class FarmerSkill extends Skill {
    public FarmerSkill() {
        super(
                Constants.FARMER_SKILL,
                Constants.FARMER_SKILL_TEX,
                Text.translatable("skill.improvableskills.farmer"),
                Text.translatable("skill.improvableskills.farmer.desc"),
                20000,
                4,
                Set.of(

                ));
    }
}
