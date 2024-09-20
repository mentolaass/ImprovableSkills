package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.shared.Constants;
import ru.mentola.improvableskills.attribute.Attributes;

import java.util.Set;

public final class MinerLuckSkill extends Skill {
    public MinerLuckSkill() {
        super(
                Constants.MINER_LUCK_SKILL,
                Constants.MINER_LUCK_SKILL_TEX,
                Text.translatable("skill.improvableskills.miner_luck"),
                Text.translatable("skill.improvableskills.miner_luck.desc"),
                5000,
                6,
                Set.of(
                        Attributes.MINER_LUCK_ATTRIBUTE_PERCENT,
                        Attributes.MINER_LUCK_ATTRIBUTE_COUNT
                )
        );
    }
}