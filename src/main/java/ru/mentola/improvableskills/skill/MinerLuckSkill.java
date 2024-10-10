package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.ModAttributes;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class MinerLuckSkill extends Skill {
    public MinerLuckSkill() {
        super(
                Constants.MINER_LUCK_SKILL,
                Constants.MINER_LUCK_SKILL_TEX,
                Text.translatable("skill.improvableskills.miner_luck"),
                Text.translatable("skill.improvableskills.miner_luck.desc"),
                5000,
                2,
                Set.of(
                        ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL,
                        ModAttributes.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP
                )
        );
    }
}