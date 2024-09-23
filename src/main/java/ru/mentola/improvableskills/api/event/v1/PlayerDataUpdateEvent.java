package ru.mentola.improvableskills.api.event.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.api.event.Cancellable;
import ru.mentola.improvableskills.api.event.Event;
import ru.mentola.improvableskills.attribute.Attribute;
import ru.mentola.improvableskills.data.PlayerData;
import ru.mentola.improvableskills.skill.Skill;

@RequiredArgsConstructor @Getter
public final class PlayerDataUpdateEvent implements Event, Cancellable {
    private final PlayerData data;
    private final Type type;
    private final UpdateData updateData;
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancelled = value;
    }

    public interface Subscribe {
        void onPlayerDataUpdate(PlayerDataUpdateEvent event);
    }

    public enum Type {
        UPGRADE_LEVEL,
        UPGRADE_ATTRIBUTE,
        ATTACH_SKILL
    }

    @AllArgsConstructor @Getter
    public static class UpdateData {
        @Nullable // not null with upgrade attribute event type
        private final Attribute<?> upgradedAttribute;
        @Nullable // not null with upgrade attribute event type
        private final Skill upgradedAttributeSkill;
        @Nullable // not null with attach skill event type
        private final Skill attachedSkill;
        // not null with upgrade level event type
        private final int level;
    }
}