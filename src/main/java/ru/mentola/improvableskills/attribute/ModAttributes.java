package ru.mentola.improvableskills.attribute;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.factory.AttributeFactory;
import ru.mentola.improvableskills.shared.Constants;

public final class ModAttributes {
    public static final NumberAttribute<Integer> MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL = AttributeFactory.createNumAttribute(
            Constants.MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL.toString(),
            Text.translatable("skill.improvableskills.miner_luck.attribute.chance_call").getString(),
            Text.translatable("skill.improvableskills.miner_luck.attribute.chance_call.desc").getString(),
            10000,
            5,
            50,
            5
    );
    public static final NumberAttribute<Integer> MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP = AttributeFactory.createNumAttribute(
            Constants.MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP.toString(),
            Text.translatable("skill.improvableskills.miner_luck.attribute.count_drop").getString(),
            Text.translatable("skill.improvableskills.miner_luck.attribute.count_drop.desc").getString(),
            80000,
            1,
            5,
            1
    );
    public static final NumberAttribute<Integer> VAMPIRISM_SKILL_ATTRIBUTE_CHANCE_CALL = AttributeFactory.createNumAttribute(
            Constants.VAMPIRISM_SKILL_ATTRIBUTE_CHANCE_CALL.toString(),
            Text.translatable("skill.improvableskills.vampirism.attribute.chance_call").getString(),
            Text.translatable("skill.improvableskills.miner_luck.attribute.chance_call.desc").getString(),
            51999,
            1,
            5,
            1
    );
    public static final NumberAttribute<Float> VAMPIRISM_SKILL_ATTRIBUTE_COUNT_RETURN = AttributeFactory.createNumAttribute(
            Constants.VAMPIRISM_SKILL_ATTRIBUTE_COUNT_RETURN.toString(),
            Text.translatable("skill.improvableskills.vampirism.attribute.count_return").getString(),
            Text.translatable("skill.improvableskills.vampirism.attribute.count_return.desc").getString(),
            55000,
            0.2f,
            4f,
            0.2f
    );
}