package ru.mentola.improvableskills.util;

import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.skill.attribute.Attribute;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@UtilityClass
public class Util {
    public boolean pointedTo(double x, double y, double width, double height, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public Set<Attribute<?>> copyAttributes(Set<Attribute<?>> t) {
        Set<Attribute<?>> attributes = new HashSet<>();
        t.forEach((attribute) -> attributes.add(attribute.copy(true)));
        return attributes;
    }

    public Set<Item> getOreDrop(Block block, int count) {
        Set<Item> items = new HashSet<>();
        Item dropItem = null;
        if (block.equals(Blocks.DIAMOND_ORE) || block.equals(Blocks.DEEPSLATE_DIAMOND_ORE)) {
            dropItem = Items.DIAMOND;
        } else if (block.equals(Blocks.GOLD_ORE) || block.equals(Blocks.DEEPSLATE_GOLD_ORE)) {
            dropItem = Items.RAW_GOLD;
        } else if (block.equals(Blocks.EMERALD_ORE) || block.equals(Blocks.DEEPSLATE_EMERALD_ORE)) {
            dropItem = Items.EMERALD;
        } else if (block.equals(Blocks.COAL_ORE) || block.equals(Blocks.DEEPSLATE_COAL_ORE)) {
            dropItem = Items.COAL;
        } else if (block.equals(Blocks.IRON_ORE) || block.equals(Blocks.DEEPSLATE_IRON_ORE)) {
            dropItem = Items.RAW_IRON;
        } else if (block.equals(Blocks.COPPER_ORE) || block.equals(Blocks.DEEPSLATE_COPPER_ORE)) {
            dropItem = Items.RAW_COPPER;
        }
        if (dropItem != null) {
            for (int i = 0; i < count; i++) {
                items.add(dropItem);
            }
        }
        return items;
    }

    public <T extends Number> T getNumberValueAttribute(Attribute<T> attribute) {
        int level = attribute.getLevel();
        double minValue = attribute.getMinValue().doubleValue();
        double maxValue = attribute.getMaxValue().doubleValue();
        double stepLevel = attribute.getStepLevel().doubleValue();
        double value = minValue + (level * stepLevel);
        if (value > maxValue) value = maxValue;
        return castToType(attribute.getMinValue(), value);
    }

    @SuppressWarnings("unchecked")
    private <T extends Number> T castToType(T exampleValue, double value) {
        if (exampleValue instanceof Integer) {
            return (T) Integer.valueOf((int) value);
        } else if (exampleValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else if (exampleValue instanceof Float) {
            return (T) Float.valueOf((float) value);
        } else if (exampleValue instanceof Long) {
            return (T) Long.valueOf((long) value);
        }
        throw new IllegalArgumentException("Unsupported number type");
    }

    public <T extends Number> int getMaxLevelAttribute(Attribute<T> attribute) {
        double minValue = attribute.getMinValue().doubleValue();
        double maxValue = attribute.getMaxValue().doubleValue();
        double stepLevel = attribute.getStepLevel().doubleValue();
        return (int) ((maxValue - minValue) / stepLevel);
    }

    public int randomNumber(int max, int min) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public int getNextPointsToNextLevelNeed(PlayerData playerData) {
        return (int) ((10000 * playerData.getLevel()) * 1.5);
    }
}