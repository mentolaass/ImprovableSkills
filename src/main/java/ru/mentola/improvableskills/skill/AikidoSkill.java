package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class AikidoSkill extends Skill {
    public AikidoSkill() {
        super(
                Constants.AIKIDO_SKILL,
                Constants.AIKIDO_SKILL_TEX,
                Text.translatable("skill.improvableskills.aikido"),
                Text.translatable("skill.improvableskills.aikido.desc"),
                24152,
                13,
                Set.of(

                ));
    }
}
