package ru.mentola.improvableskills.shared;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

public final class Constants {
    public static final Identifier POINTS_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/point/point_tex.png");

    // skills
    public static final Identifier MINER_LUCK_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "miner_luck_skill");
    public static final Identifier MINER_LUCK_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/luck_mine_skill_tex.png");
    public static final Identifier MINER_LUCK_ATTRIBUTE_PERCENT = Identifier.of(ImprovableSkills.MOD_ID,"miner_luck_attribute_percent");
    public static final Identifier MINER_LUCK_ATTRIBUTE_COUNT = Identifier.of(ImprovableSkills.MOD_ID,"miner_luck_attribute_count");

    public static final Identifier VAMPIRISM_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "vampirism_skill");
    public static final Identifier VAMPIRISM_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/vampirism_skill_tex.png");
    public static final Identifier VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH = Identifier.of(ImprovableSkills.MOD_ID,"vampirism_attribute_percent");
    public static final Identifier VAMPIRISM_ATTRIBUTE_CHANCE = Identifier.of(ImprovableSkills.MOD_ID,"vampirism_attribute_chance");

    public static final Identifier RABBIT_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "rabbit_skill");
    public static final Identifier RABBIT_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/rabbit_skill_tex.png");
    public static final Identifier RABBIT_ATTRIBUTE_SPEED = Identifier.of(ImprovableSkills.MOD_ID, "rabbit_attribute_speed");
    public static final Identifier RABBIT_ATTRIBUTE_POWER_JUMP = Identifier.of(ImprovableSkills.MOD_ID, "rabbit_attribute_power_jump");

    public static final Identifier MASTER_WEAPON_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "master_weapon_skill");
    public static final Identifier MASTER_WEAPON_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/master_weapon_skill_tex.png");
    public static final Identifier MASTER_WEAPON_ATTRIBUTE_DAMAGE = Identifier.of(ImprovableSkills.MOD_ID, "master_weapon_attribute_damage");

    public static final Identifier BEHOLDER_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "beholder_skill");
    public static final Identifier BEHOLDER_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/beholder_skill_tex.png");

    public static final Identifier FARMER_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "farmer_skill");
    public static final Identifier FARMER_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/farmer_skill_tex.png");

    public static final Identifier WOODCUTTER_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "woodcutter_skill");
    public static final Identifier WOODCUTTER_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/woodcutter_skill_tex.png");

    public static final Identifier FISHBREATH_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "fishbreath_skill");
    public static final Identifier FISHBREATH_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/fishbreath_skill_tex.png");

    public static final Identifier STARNAVIGATOR_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "starnavigator_skill");
    public static final Identifier STARNAVIGATOR_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/starnavigator_skill_tex.png");

    public static final Identifier CREED_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "creed_skill");
    public static final Identifier CREED_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/creed_skill_tex.png");

    public static final Identifier HARDNESS_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "hardness_skill");
    public static final Identifier HARDNESS_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/hardness_skill_tex.png");

    public static final Identifier ELOQUENCE_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "eloquence_skill");
    public static final Identifier ELOQUENCE_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/eloquence_skill_tex.png");

    public static final Identifier MERCHANTHAND_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "merchanthand_skill");
    public static final Identifier MERCHANTHAND_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/merchanthand_skill_tex.png");

    public static final Identifier AIKIDO_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "aikido_skill");
    public static final Identifier AIKIDO_SKILL_TEX = Identifier.of(ImprovableSkills.MOD_ID, "textures/aikido_skill_tex.png");

    // translates
    public static final Text UPGRADE_ATTRIBUTES = Text.translatable("gui.improvableskills.improveattributesscreen.title");
    public static final Text PRESS_TO_MORE = Text.translatable("gui.improvableskills.improvescreen.presstomore");
    public static final Text INFO_SKILL = Text.translatable("gui.improvableskills.improvescreen.infoskill");
    public static final Text ATTRIBUTES = Text.translatable("gui.improvableskills.improvescreen.attributes");
    public static final Text ATTACHED = Text.translatable("gui.improvableskills.improvescreen.attached");
    public static final Text ATTACH = Text.translatable("gui.improvableskills.improvescreen.attach");
    public static final Text LESS_POINTS = Text.translatable("gui.improvableskills.improvescreen.lesspoints");
    public static final Text NEED_TO_ATTACH = Text.translatable("gui.improvableskills.improvescreen.needtoattach");
    public static final Text UPGRADE = Text.translatable("gui.improvableskills.improvescreen.upgrade");
    public static final Text YOUR_POINTS = Text.translatable("gui.improvableskills.improvescreen.yourpoints");
    public static final Text YOUR_LEVEL = Text.translatable("gui.improvableskills.improvescreen.yourlevel");
    public static final Text APPLY = Text.translatable("gui.improvableskills.improveattributesscreen.apply");
    public static final Text NEED = Text.translatable("gui.improvableskills.improveattributesscreen.need");
    public static final Text POINTS = Text.translatable("gui.improvableskills.other.points");
    public static final Text LEVEL = Text.translatable("gui.improvableskills.other.level");
    public static final Text MINER_LUCK_ATTRIBUTE_PERCENT_TRANSLATE = Text.translatable("skill.improvableskills.miner_luck_attribute_percent");
    public static final Text MINER_LUCK_ATTRIBUTE_COUNT_TRANSLATE = Text.translatable("skill.improvableskills.miner_luck_attribute_count");
    public static final Text VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH_TRANSLATE = Text.translatable("skill.improvableskills.vampirism_attribute_percent");
    public static final Text VAMPIRISM_ATTRIBUTE_CHANCE_TRANSLATE = Text.translatable("skill.improvableskills.vampirism_attribute_chance");
    public static final Text RABBIT_ATTRIBUTE_SPEED_TRANSLATE = Text.translatable("skill.improvableskills.rabbit_attribute_speed");
    public static final Text RABBIT_ATTRIBUTE_POWER_JUMP_TRANSLATE = Text.translatable("skill.improvableskills.rabbit_attribute_power_jump");
    public static final Text MASTER_WEAPON_ATTRIBUTE_DAMAGE_TRANSLATE = Text.translatable("skill.improvableskills.master_weapon_attribute_damage");
}