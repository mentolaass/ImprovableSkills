package ru.mentola.improvableskills.api.event.v1;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mentola.improvableskills.api.event.Cancellable;
import ru.mentola.improvableskills.api.event.Event;
import ru.mentola.improvableskills.point.PointType;

@RequiredArgsConstructor @Getter
public final class PlayerGetPointsEvent implements Event, Cancellable {
    private final int pointsCount;
    private final PointType pointType;
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
        void onPlayerGetPoints(PlayerGetPointsEvent event);
    }
}