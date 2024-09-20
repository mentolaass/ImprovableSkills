package ru.mentola.improvableskills.client.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import ru.mentola.improvableskills.ImprovableSkills;

public final class CustomSound {
    public static SoundEvent UP_ATTRIBUTE;
    public static SoundEvent UP_LEVEL;
    public static SoundEvent ATTACH_SKILL;

    public static void initialize() {
        UP_ATTRIBUTE = registerSound("up_attribute");
        UP_LEVEL = registerSound("up_level");
        ATTACH_SKILL = registerSound("attach_skill");
    }

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(ImprovableSkills.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }
}