package ru.mentola.improvableskills.client.notice;

import net.minecraft.client.gui.DrawContext;

import java.util.HashSet;
import java.util.Set;

public final class NoticeQueue {
    private static final Set<Notice> SOURCE = new HashSet<>();

    public static void addToQueue(Notice notice) {
        SOURCE.add(notice);
    }

    public static void render(DrawContext context) {
        SOURCE.removeIf((notice) -> !notice.isAlive());
        for (Notice notice : SOURCE) notice.render(context);
    }
}