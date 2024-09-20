package ru.mentola.improvableskills.attribute;

import ru.mentola.improvableskills.shared.Constants;

public final class Attributes {
    public static final NumberAttribute<Integer> MINER_LUCK_ATTRIBUTE_PERCENT = new NumberAttribute<>(
            Constants.MINER_LUCK_ATTRIBUTE_PERCENT,
            Constants.MINER_LUCK_ATTRIBUTE_PERCENT_TRANSLATE,
            2000,
            30,
            100,
            5
    );

    public static final NumberAttribute<Integer> MINER_LUCK_ATTRIBUTE_COUNT = new NumberAttribute<>(
            Constants.MINER_LUCK_ATTRIBUTE_COUNT,
            Constants.MINER_LUCK_ATTRIBUTE_COUNT_TRANSLATE,
            15000,
            1,
            3,
            1
    );

    public static final NumberAttribute<Integer> VAMPIRISM_ATTRIBUTE_CHANCE = new NumberAttribute<>(
            Constants.VAMPIRISM_ATTRIBUTE_CHANCE,
            Constants.VAMPIRISM_ATTRIBUTE_CHANCE_TRANSLATE,
            10000,
            1,
            10,
            1
    );

    public static final NumberAttribute<Integer> VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH = new NumberAttribute<>(
            Constants.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH,
            Constants.VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH_TRANSLATE,
            15000,
            1,
            5,
            1
    );

    public static final NumberAttribute<Integer> RABBIT_ATTRIBUTE_POWER_JUMP = new NumberAttribute<>(
            Constants.RABBIT_ATTRIBUTE_POWER_JUMP,
            Constants.RABBIT_ATTRIBUTE_POWER_JUMP_TRANSLATE,
            50000,
            2,
            5,
            1
    );

    public static final NumberAttribute<Integer> RABBIT_ATTRIBUTE_SPEED = new NumberAttribute<>(
            Constants.RABBIT_ATTRIBUTE_SPEED,
            Constants.RABBIT_ATTRIBUTE_SPEED_TRANSLATE,
            30000,
            1,
            3,
            1
    );

    public static final NumberAttribute<Float> MASTER_WEAPON_ATTRIBUTE_DAMAGE = new NumberAttribute<>(
            Constants.MASTER_WEAPON_ATTRIBUTE_DAMAGE,
            Constants.MASTER_WEAPON_ATTRIBUTE_DAMAGE_TRANSLATE,
            30000,
            0.2f,
            5f,
            0.2f
    );
}