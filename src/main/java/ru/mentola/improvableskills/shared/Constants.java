package ru.mentola.improvableskills.shared;

import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

public final class Constants {
    public static final int LEVEL_UP_STEP = 10000;
    public static final double LEVEL_UP_MODIFIER = 1.5;

    public static final Identifier POINTS_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/ui/point_tex.png");
    public static final Identifier WARNING_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/ui/warning_tex.png");
    public static final Identifier LEVEL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/ui/level_tex.png");
    public static final Identifier ATTRIBUTE_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/ui/attribute_tex.png");
    public static final Identifier DESCRIPTION_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/ui/description_tex.png");

    // skills
    public static final Identifier MINER_LUCK_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "miner_luck_skill");
    public static final Identifier MINER_LUCK_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/luck_mine_skill_tex.png");
    public static final Identifier MINER_LUCK_SKILL_ATTRIBUTE_CHANCE_CALL = Identifier.of(ImprovableSkills.MOD_ID, "miner_luck_skill_attribute_chance_call");
    public static final Identifier MINER_LUCK_SKILL_ATTRIBUTE_COUNT_DROP = Identifier.of(ImprovableSkills.MOD_ID, "miner_luck_skill_attribute_count_drop");

    public static final Identifier VAMPIRISM_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "vampirism_skill");
    public static final Identifier VAMPIRISM_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/vampirism_skill_tex.png");
    public static final Identifier VAMPIRISM_SKILL_ATTRIBUTE_CHANCE_CALL = Identifier.of(ImprovableSkills.MOD_ID, "vampirism_skill_attribute_chance_call");
    public static final Identifier VAMPIRISM_SKILL_ATTRIBUTE_COUNT_RETURN = Identifier.of(ImprovableSkills.MOD_ID, "vampirism_skill_attribute_count_return");
}