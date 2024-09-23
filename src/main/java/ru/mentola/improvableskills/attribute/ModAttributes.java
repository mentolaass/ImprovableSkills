package ru.mentola.improvableskills.attribute;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.factory.AttributeFactory;
import ru.mentola.improvableskills.shared.Constants;

public final class ModAttributes {
    public static final NumberAttribute<Integer> MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL = AttributeFactory.createNumAttribute(
            Constants.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL,
            Text.translatable("skill.improvableskills.miner_luck.attribute.chance_call"),
            Text.translatable("skill.improvableskills.miner_luck.attribute.chance_call.desc"),
            10000,
            5,
            50,
            5
    );
    public static final NumberAttribute<Integer> MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP = AttributeFactory.createNumAttribute(
            Constants.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP,
            Text.translatable("skill.improvableskills.miner_luck.attribute.count_drop"),
            Text.translatable("skill.improvableskills.miner_luck.attribute.count_drop.desc"),
            30000,
            1,
            5,
            1
    );
}