package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.skill.attribute.Attributes;
import ru.mentola.improvableskills.skill.base.Skill;

import java.util.Set;

public final class MinerLuckSkill extends Skill {
    public MinerLuckSkill() {
        super(
                Constants.MINER_LUCK_SKILL,
                Text.translatable("skill.improvableskills.miner_luck"),
                Text.translatable("skill.improvableskills.miner_luck.desc"),
                1000,
                1,
                Set.of(Attributes.MINER_LUCK_ATTRIBUTE_PERCENT.copy(),
                        Attributes.MINER_LUCK_ATTRIBUTE_COUNT.copy())
        );
    }
}