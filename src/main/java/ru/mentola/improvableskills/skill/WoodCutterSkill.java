package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class WoodCutterSkill extends Skill {
    public WoodCutterSkill() {
        super(
                Constants.WOODCUTTER_SKILL,
                Constants.WOODCUTTER_SKILL_TEX,
                Text.translatable("skill.improvableskills.woodcutter"),
                Text.translatable("skill.improvableskills.woodcutter.desc"),
                10000,
                2,
                Set.of(

                ));
    }
}
