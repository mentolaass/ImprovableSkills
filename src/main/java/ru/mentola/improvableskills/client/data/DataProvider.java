package ru.mentola.improvableskills.client.data;

import org.jetbrains.annotations.Nullable;
import ru.mentola.improvableskills.data.Data;
import ru.mentola.improvableskills.data.PlayerData;

import java.util.HashSet;
import java.util.Set;

public final class DataProvider {
    private static final Set<Data> registeredData = new HashSet<>();

    public DataProvider() {
        throw new RuntimeException("Unsupported");
    }

    public static void initialize() {
        add(new PlayerData());
    }

    @Nullable
    public static <D extends Data> D get(Class<D> target) {
        return registeredData.stream()
                .filter((data) -> data.getClass().equals(target))
                .findFirst()
                .map(target::cast)
                .orElse(null);
    }

    private static <D extends Data> void add(D data) {
        if (get(data.getClass()) != null)
            return;
        registeredData.add(data);
    }
}