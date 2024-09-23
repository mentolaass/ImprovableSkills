package ru.mentola.improvableskills.api.event.manager;

import ru.mentola.improvableskills.api.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class EventManager {
    private final Set<Listener> registeredListeners = new HashSet<>();

    public void registerListener(Listener listener) {
        this.registeredListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        this.registeredListeners.remove(listener);
    }

    public Set<Listener> getRegisteredListeners() {
        return Collections.unmodifiableSet(this.registeredListeners);
    }
}