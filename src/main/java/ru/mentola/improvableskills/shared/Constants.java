package ru.mentola.improvableskills.shared;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

public final class Constants {
    // skills
    public static final Identifier MINER_LUCK_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "miner_luck_skill");
    public static final Identifier MINER_LUCK_ATTRIBUTE_PERCENT = Identifier.of(ImprovableSkills.MOD_ID,"miner_luck_attribute_percent");
    public static final Identifier MINER_LUCK_ATTRIBUTE_COUNT = Identifier.of(ImprovableSkills.MOD_ID,"miner_luck_attribute_count");

    public static final Identifier VAMPIRISM_SKILL = Identifier.of(ImprovableSkills.MOD_ID, "vampirism_skill");
    public static final Identifier VAMPIRISM_ATTRIBUTE_PERCENT_HEALTH = Identifier.of(ImprovableSkills.MOD_ID,"vampirism_attribute_percent");
    public static final Identifier VAMPIRISM_ATTRIBUTE_CHANCE = Identifier.of(ImprovableSkills.MOD_ID,"vampirism_attribute_chance");

    // attributes
    public static final int LEVEL_UP_ATTRIBUTE_PRICE = 15000;

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
}