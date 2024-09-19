package ru.mentola.improvableskills.skill.attribute;

import ru.mentola.improvableskills.shared.Constants;

public final class Attributes {
    public static final NumberAttribute<Integer> MINER_LUCK_ATTRIBUTE_PERCENT = new NumberAttribute<>(
            Constants.MINER_LUCK_ATTRIBUTE_PERCENT,
            Constants.MINER_LUCK_ATTRIBUTE_PERCENT_TRANSLATE,
            30,
            100,
            5
    );

    public static final NumberAttribute<Integer> MINER_LUCK_ATTRIBUTE_COUNT = new NumberAttribute<>(
            Constants.MINER_LUCK_ATTRIBUTE_COUNT,
            Constants.MINER_LUCK_ATTRIBUTE_COUNT_TRANSLATE,
            1,
            3,
            1
    );

    public static final NumberAttribute<Integer> VAMPIRISM_ATTRIBUTE_CHANCE = new NumberAttribute<>(
            Constants.VAMPIRISM_ATTRIBUTE_CHANCE,
            Constants.VAMPIRISM_ATTRIBUTE_CHANCE_TRANSLATE,
            1,
            10,
            1
    );

    public static final NumberAttribute<Integer> VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH = new NumberAttribute<>(
            Constants.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH,
            Constants.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH_TRANSLATE,
            1,
            5,
            1
    );

}