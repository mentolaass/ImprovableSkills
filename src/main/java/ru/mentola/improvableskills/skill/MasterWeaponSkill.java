package ru.mentola.improvableskills.skill;

import net.minecraft.text.Text;
import ru.mentola.improvableskills.attribute.Attributes;
import ru.mentola.improvableskills.shared.Constants;

import java.util.Set;

public final class MasterWeaponSkill extends Skill {
    public MasterWeaponSkill() {
        super(
                Constants.MASTER_WEAPON_SKILL,
                Constants.MASTER_WEAPON_SKILL_TEX,
                Text.translatable("skill.improvableskills.master_weapon"),
                Text.translatable("skill.improvableskills.master_weapon.desc"),
                10000,
                10,
                Set.of(
                        Attributes.MASTER_WEAPON_ATTRIBUTE_DAMAGE
                )
        );
    }
}
