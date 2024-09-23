package ru.mentola.improvableskills.api.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean value);
}