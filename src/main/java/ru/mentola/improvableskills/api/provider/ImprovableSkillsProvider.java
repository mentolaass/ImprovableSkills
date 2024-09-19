package ru.mentola.improvableskills.api.provider;

import lombok.Getter;
import lombok.Setter;
import ru.mentola.improvableskills.api.ImprovableSkillsAPI;

public final class ImprovableSkillsProvider {
    @Getter @Setter
    private static ImprovableSkillsAPI API;
}