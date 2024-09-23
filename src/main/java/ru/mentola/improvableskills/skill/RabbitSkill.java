package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class RabbitSkill extends Skill {
    public RabbitSkill() {
        super(
                Constants.RABBIT_SKILL,
                Constants.RABBIT_SKILL_TEX,
                Text.translatable("skill.improvableskills.rabbit"),
                Text.translatable("skill.improvableskills.rabbit.desc"),
                14444,
                5,
                Set.of(

                ));
    }
}